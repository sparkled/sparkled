---
sessionId: session-260806-145839-yqgw
---

# Requirements

### Overview & Goals
Replace the current placeholder `webui/app/page.tsx` (default HeroUI template content) with a real **Dashboard / Home page** for Sparkled. The page is the app's landing page: it shows high-level system status and gives quick access to the four core entity types, plus lets the user adjust global LED brightness without leaving the page.

### Scope
**In scope**
- Rebuild `app/page.tsx` as a client-rendered dashboard using `useApiGetDashboard()` (from `webui/hooks/api/useApi.ts`).
- A brightness control backed by `GET/PUT /api/settings/BRIGHTNESS` (`useApiGetSetting` / `useApiUpdateSetting`).
- Five summary cards: **Stages, Songs, Sequences, Playlists, Scheduled Tasks** (the last one added because the `/dashboard` payload already includes it).
- Quick playback actions (Play/Stop) for sequences and playlists directly from the dashboard, via `useApiAdjustPlayback()` / `POST /api/player`.
- Minimal placeholder pages (`/stages`, `/songs`, `/sequences`, `/playlists`, `/scheduled-tasks`) so each card's "View all" link is a real, working navigation target (full list/detail pages are out of scope — future work).
- Responsive layout (mobile → desktop), dark theme (already the app default), modern/slick visual style consistent with the existing HeroUI template tokens (`bg-surface`, `text-accent`, `text-muted`, `border-separator`, etc.).
- Minor branding cleanup: replace leftover template placeholders ("ACME", "Next.js + HeroUI", default nav items) with Sparkled-appropriate values, only where trivial and low-risk.

**Out of scope**
- Full CRUD pages for stages/songs/sequences/playlists (create/edit/delete modals) — only the dashboard summary and stub routes.
- Authentication (none required, per API).
- Scheduled task creation UI.

### User Stories
- As a user, I want to see at a glance how many stages, songs, sequences, playlists and scheduled tasks exist, so I understand the current state of my setup.
- As a user, I want to see the most recent/relevant items in each category directly on the dashboard, so I don't need to open a separate page for common information.
- As a user, I want to adjust the global brightness from the home page, so I can quickly dim/brighten my display without navigating elsewhere.
- As a user, I want to play or stop a sequence/playlist directly from the dashboard, so I can control playback without extra clicks.
- As a mobile user, I want the dashboard to reflow into a single column and remain fully usable on a small screen.

### Functional Requirements
- **Dashboard data**: On load, call `useApiGetDashboard()`; while loading, show skeleton placeholders per card; on error, show an inline error state (no crash).
- **Cards**: Each of the 5 cards shows an icon, title, a count badge (e.g. "12"), and a list of up to ~4 most relevant items (see Technical Design for per-entity fields), plus a "View all" link to its placeholder route. If a category is empty, show a friendly empty state instead of an empty list.
- **Brightness slider**: Range 0–255 (matches backend `SettingsConstants.Brightness`), displayed to the user as 0–100%. Dragging updates the visual value immediately; releasing the thumb persists the change via `useApiUpdateSetting()`. Initial value is fetched from `GET /api/settings/BRIGHTNESS` (auto-created server-side on first access).
- **Quick playback actions**: Each sequence/playlist item has Play and Stop controls that call `useApiAdjustPlayback()` with the appropriate `PlaylistActionViewModel` (`PLAY_SEQUENCE`/`PLAY_PLAYLIST`/`STOP`).
- **Placeholder routes**: `/stages`, `/songs`, `/sequences`, `/playlists`, `/scheduled-tasks` each render a minimal "coming soon" page with a link back to the dashboard, so links are functional, not dead.

### Non-Functional Requirements
- Fully responsive: single column on mobile, multi-column grid on tablet/desktop.
- Dark theme by default (already configured in `app/layout.tsx`); use existing HeroUI theme tokens rather than hardcoded colors.
- No new runtime dependencies beyond what's already installed (`@heroui/react`, `swr`, `clsx`, `tailwind-variants`).

# Technical Design

### Current Implementation
- `webui/app/page.tsx` is still the unmodified HeroUI starter template (hero title + doc/GitHub links).
- `webui/app/layout.tsx` already wraps everything in `Providers` with `defaultTheme: "dark"`, and renders a persistent `Navbar` (`webui/components/navbar.tsx`) + footer.
- `webui/hooks/api/useApi.ts` already exposes all needed hooks: `useApiGetDashboard`, `useApiGetSetting`, `useApiUpdateSetting`, `useApiAdjustPlayback` (types from `webui/src/types/viewModels.ts`: `DashboardViewModel`, `SettingViewModel`/`SettingEditViewModel`, `PlaylistActionViewModel`).
- `webui/components/primitives.ts` defines `title`/`subtitle` `tailwind-variants` helpers; `webui/components/icons.tsx` defines hand-written inline SVG icon components.
- Lucide is now the project's chosen icon package (`lucide@1.28.0` is present in `webui/package.json`). That package is the vanilla/DOM flavor of Lucide, so the React bindings package `lucide-react` will additionally be added (a trivial, low-risk dependency addition consistent with the same icon set) and used directly for all new dashboard icons, rather than extending the hand-drawn `icons.tsx` file.
- HeroUI (`@heroui/react` 3.2.3) provides `Card`, `Slider`, `Button`, `Chip`, `Skeleton`, `Alert`, `Empty State`, `Surface`, `Link` — all usable directly, no `HeroUIProvider` wrapper is required (confirmed: none is used elsewhere in the app).
- The old AngularJS-era app (`webui-old/src/screens/dashboard/DashboardScreen.tsx`) is a useful reference for *what* to show per card (title/subtitle per item, status coloring for sequences, play/stop actions) but uses a completely different stack (Redux, react-grid-system, styled-components) — not to be reused directly.

### Key Decisions
1. **Card content shape**: count header + top ~4 items + "View all" link (per user decision), rather than count-only buttons or full inline lists.
2. **Navigation**: add minimal placeholder route pages so "View all" links work now, rather than leaving them non-interactive (per user decision).
3. **Scheduled Tasks card included** as a 5th card, matching the full `DashboardViewModel` payload (per user decision), even though the original ask only named 4 categories.
4. **Generic, reusable dashboard components**: build one parameterized `DashboardPanel` (card) + `DashboardPanelItem` (row) rather than 5 near-duplicate components, since the 5 cards only differ in data/icon/actions, not structure.
5. **Brightness persistence strategy**: use local component state for live slider feedback while dragging, and only call the mutation (`useApiUpdateSetting`) `onChangeEnd` to avoid flooding the API with PUT requests per drag tick.

### Proposed Changes
- **`webui/app/page.tsx`** (rewritten): `"use client"` component. Renders a page header (`title()` from `primitives.ts` + brightness control) and a responsive grid (`grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6`) of `DashboardPanel`s fed from `useApiGetDashboard()`.
- **`webui/components/dashboard/BrightnessControl.tsx`** (new): wraps HeroUI `Slider`, bound to `useApiGetSetting('BRIGHTNESS')` for the initial value and `useApiUpdateSetting()` to persist changes; displays value as a percentage; shows a skeleton while the initial GET is in flight.
- **`webui/components/dashboard/DashboardPanel.tsx`** (new): generic card taking `title`, `icon`, `count`, `viewAllHref`, `items`, `renderItem`, `isLoading`, `emptyLabel`. Uses HeroUI `Card`/`Skeleton`/empty-state pattern.
- **`webui/components/dashboard/DashboardPanelItem.tsx`** (new): generic row (title + subtitle + optional trailing actions/status chip) reused by all 5 panels.
- **`webui/components/dashboard/PlaybackActions.tsx`** (new): small Play/Stop `Button` pair, calling `useApiAdjustPlayback()` with a `PlaylistActionViewModel` built from props (`sequenceId` or `playlistId`).
- **`webui/utils/format.ts`** (new): `formatDuration(durationMs: number): string` (`mm:ss`), used by Songs/Sequences/Playlists panels — ported logic from `webui-old/src/utils/dateUtils.ts` but written fresh for this codebase.
- **Lucide icons** (new dependency `lucide-react`): used directly inside the dashboard components for Stage/Song/Sequence/Playlist/ScheduledTask category icons (e.g. `Home`, `Music`, `AudioLines`, `ListMusic`, `CalendarClock`) and for actions (`Play`, `Square` for stop, `Sun` for brightness) — no changes needed to the existing hand-drawn `webui/components/icons.tsx`.
- **Placeholder routes** (new): `webui/app/stages/page.tsx`, `webui/app/songs/page.tsx`, `webui/app/sequences/page.tsx`, `webui/app/playlists/page.tsx`, `webui/app/scheduled-tasks/page.tsx` — each a minimal server component with a heading and a link back to `/`.
- **Branding touch-ups**: update `webui/config/site.ts` (`name`) and the hardcoded "ACME" text in `webui/components/navbar.tsx` to Sparkled branding (low-risk, cosmetic only).

### Data Models / Contracts
Per-card data mapping (from `DashboardViewModel`, already typed in `viewModels.ts`):
```ts
// Stages: StageSummaryViewModel { id, name }
// -> title = name, no subtitle, no quick action, link -> /stages

// Songs: SongViewModel { id, name, artist?, durationMs }
// -> title = name, subtitle = `${artist ?? 'Unknown artist'} · ${formatDuration(durationMs)}`

// Sequences: SequenceSummaryViewModel { id, name, stageName, songName, durationMs, status }
// -> title = name, subtitle = `${stageName} · ${songName} · ${formatDuration(durationMs)}`
// -> status chip colored by `status` (NEW/DRAFT/PUBLISHED)
// -> actions: Play (PLAY_SEQUENCE, sequenceId), Stop (STOP)

// Playlists: PlaylistSummaryViewModel { id, name, sequenceCount, durationMs }
// -> title = name, subtitle = `${sequenceCount} sequence(s) · ${formatDuration(durationMs)}`
// -> actions: Play (PLAY_PLAYLIST, playlistId), Stop (STOP)

// Scheduled tasks: ScheduledTaskSummaryViewModel { id, cronExpression, type, playlistId?, playlistName? }
// -> title = human label per `type` (e.g. `Play playlist ${playlistName}`, 'Set brightness', 'Stop playback')
// -> subtitle = `Cron (${cronExpression})`
```
Brightness: `useApiGetSetting('BRIGHTNESS')` returns `SettingViewModel { id: 'BRIGHTNESS', value: string }` (0–255 as string); `useApiUpdateSetting()` takes `{ id: 'BRIGHTNESS', setting: { value: string } }`.

### Components
```mermaid
graph TD
    Page[app/page.tsx Dashboard] --> Brightness[BrightnessControl]
    Page --> Panel1[DashboardPanel: Stages]
    Page --> Panel2[DashboardPanel: Songs]
    Page --> Panel3[DashboardPanel: Sequences]
    Page --> Panel4[DashboardPanel: Playlists]
    Page --> Panel5[DashboardPanel: Scheduled Tasks]
    Panel3 --> Item1[DashboardPanelItem + PlaybackActions]
    Panel4 --> Item2[DashboardPanelItem + PlaybackActions]
    Panel1 --> Item3[DashboardPanelItem]
    Panel2 --> Item4[DashboardPanelItem]
    Panel5 --> Item5[DashboardPanelItem]
    Page --> Hook1[useApiGetDashboard]
    Brightness --> Hook2[useApiGetSetting / useApiUpdateSetting]
    Item1 -.View all.-> Stub1[/stages placeholder]
    Item2 -.View all.-> Stub2[/songs placeholder]
    Item3 -.View all.-> Stub3[/sequences placeholder]
    Item4 -.View all.-> Stub4[/playlists placeholder]
    Item5 -.View all.-> Stub5[/scheduled-tasks placeholder]
```

### File Structure
```
webui/
  app/
    page.tsx                     (rewritten)
    stages/page.tsx              (new, placeholder)
    songs/page.tsx               (new, placeholder)
    sequences/page.tsx           (new, placeholder)
    playlists/page.tsx           (new, placeholder)
    scheduled-tasks/page.tsx     (new, placeholder)
  components/
    dashboard/
      BrightnessControl.tsx      (new)
      DashboardPanel.tsx         (new)
      DashboardPanelItem.tsx     (new)
      PlaybackActions.tsx        (new)
    navbar.tsx                   (minor branding tweak)
  config/site.ts                 (minor branding tweak)
  utils/format.ts                (new)
  package.json                   (add `lucide-react` dependency)
```

### Risks
- HeroUI 3.x `Slider`/`Card` APIs are built on `react-aria-components`; prop names (`onChangeEnd`, `minValue`/`maxValue`) must be double-checked against the installed version during implementation.
- Only the vanilla `lucide` package (DOM-oriented) is currently installed; `lucide-react` needs to be added as a dependency to use Lucide icons idiomatically as React components — a small, low-risk addition since it ships the same icon set.
- `ScheduledTaskSummaryViewModel.type` can be `SET_BRIGHTNESS` or other values without a `playlistName` — label mapping must handle all `ScheduledActionType` values gracefully (fallback label).

# Testing

### Validation Approach
The `webui` project has no automated test runner configured (no Jest/Playwright in `package.json`), so validation relies on static checks plus manual/agent-driven review of the rendered behavior and code paths.

### Key Scenarios
- Dashboard loads and renders 5 panels with correct counts/items once `useApiGetDashboard()` resolves.
- Brightness slider reflects the fetched value on load and issues a single `PUT /api/settings/BRIGHTNESS` on release, not on every drag tick.
- Play/Stop actions call `useApiAdjustPlayback()` with the correct `PlaylistActionViewModel` shape for both sequences and playlists.
- Each panel's "View all" link navigates to its corresponding placeholder route.
- Layout collapses to a single column at mobile widths and expands to a multi-column grid at desktop widths.

### Edge Cases
- Dashboard fetch failure — verify an inline error state is shown instead of a crash.
- Empty category (e.g. zero playlists) — verify the empty-state message renders instead of a blank card.
- `ScheduledTaskSummaryViewModel.type` values without `playlistName` (e.g. `SET_BRIGHTNESS`, `STOP_PLAYBACK`) — verify a sensible fallback label is shown.

### Test Changes
- Run `tsc --noEmit` across `webui` to confirm no type errors.
- Run `prettier --check` (and `eslint` if runnable in the environment) on all new/changed files.
- No new automated tests are added, consistent with the project's current lack of a test harness.

# Delivery Steps

###   Step 1: Add dashboard formatting utils and reusable panel/card components
Reusable, data-agnostic building blocks exist for rendering any dashboard category as a card with a list of items.
- Add `webui/utils/format.ts` with a `formatDuration(durationMs: number): string` helper (mm:ss).
- Add `webui/components/dashboard/DashboardPanel.tsx`: a generic HeroUI `Card`-based component accepting `title`, `icon`, `count`, `viewAllHref`, `items`, `renderItem`, `isLoading`, `emptyLabel`, with skeleton and empty states.
- Add `webui/components/dashboard/DashboardPanelItem.tsx`: a generic row component (title, subtitle, optional status chip, optional trailing actions slot).
- Add the `lucide-react` dependency and use Lucide icons directly for the category icons (Stage, Song, Sequence, Playlist, Scheduled Task) and actions (Play, Stop, Brightness), rather than extending `webui/components/icons.tsx`.

###   Step 2: Implement brightness control and playback quick actions
The brightness slider and sequence/playlist play/stop controls are functional, reusable components wired to the API.
- Add `webui/components/dashboard/BrightnessControl.tsx` using HeroUI `Slider`, backed by `useApiGetSetting('BRIGHTNESS')` for the initial value and `useApiUpdateSetting()` on `onChangeEnd`, displaying the value as a percentage with a loading skeleton.
- Add `webui/components/dashboard/PlaybackActions.tsx` with Play/Stop `Button`s that call `useApiAdjustPlayback()` with the right `PlaylistActionViewModel` for a given `sequenceId` or `playlistId`.
- Both components are self-contained and independently usable so the main page (next stage) only wires props/data into them.

###   Step 3: Build the dashboard page composition
The home page (`/`) renders a full dashboard: header with brightness control and five responsive panels populated from live API data.
- Rewrite `webui/app/page.tsx` as a `"use client"` component calling `useApiGetDashboard()`.
- Render a header row containing the page title and `BrightnessControl`.
- Render a responsive grid (`grid-cols-1 sm:grid-cols-2 lg:grid-cols-3`) of five `DashboardPanel`s: Stages, Songs, Sequences, Playlists, Scheduled Tasks, each mapped from `DashboardViewModel` fields per the field mapping in the Technical Design.
- Wire `PlaybackActions` into the Sequences and Playlists panel items.
- Handle top-level loading (skeletons already inside `DashboardPanel`) and error states (inline error message on fetch failure).

###   Step 4: Add placeholder routes and polish branding/responsiveness
Every "View all" link on the dashboard resolves to a real page, and the app's placeholder branding is replaced with Sparkled-specific text.
- Add minimal placeholder pages: `webui/app/stages/page.tsx`, `webui/app/songs/page.tsx`, `webui/app/sequences/page.tsx`, `webui/app/playlists/page.tsx`, `webui/app/scheduled-tasks/page.tsx`, each with a heading and a link back to `/`.
- Update `webui/config/site.ts` (`name`) and the hardcoded "ACME" label in `webui/components/navbar.tsx` to Sparkled branding.
- Do a final responsive pass on the dashboard grid/header (mobile stacking, spacing) and verify dark-theme styling uses existing HeroUI tokens (`bg-surface`, `text-muted`, `text-accent`, `border-separator`) rather than hardcoded colors.
- Run `tsc --noEmit` and `prettier --check` across `webui` to confirm the change set is clean.
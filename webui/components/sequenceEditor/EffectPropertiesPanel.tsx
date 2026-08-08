'use client'

import {
  BlendModeValues,
  Easing,
  Effect,
  EditorItemViewModel,
  Fill,
  Param,
  ReferenceDataViewModel,
} from '@/src/types/viewModels'
import {
  Button,
  Card,
  Input,
  Label,
  ListBox,
  NumberField,
  Select,
  Switch,
  TextField,
} from '@heroui/react'
import { Trash2 } from 'lucide-react'

export type EffectPropertiesPanelProps = {
  effect: Effect | null
  maxFrame: number
  referenceData?: ReferenceDataViewModel
  onChange: (changes: Partial<Effect>) => void
  onDelete: () => void
}

export function EffectPropertiesPanel({
  effect,
  maxFrame,
  referenceData,
  onChange,
  onDelete,
}: EffectPropertiesPanelProps) {
  return (
    <Card className='flex h-full w-80 shrink-0 flex-col overflow-hidden rounded-none rounded-r-xl border-0 border-l'>
      <Card.Header className='flex flex-row items-center justify-between'>
        <Card.Title>Effect properties</Card.Title>
        {effect && (
          <Button isIconOnly aria-label='Delete effect' size='sm' variant='ghost' onPress={onDelete}>
            <Trash2 size={16} />
          </Button>
        )}
      </Card.Header>

      {!effect || !referenceData ? (
        <Card.Content>
          <p className='text-muted text-sm'>Select an effect on the timeline to edit its properties.</p>
        </Card.Content>
      ) : (
        <Card.Content className='flex flex-col gap-4 overflow-y-auto'>
          <div className='grid grid-cols-2 gap-3'>
            <NumberField
              maxValue={effect.endFrame}
              minValue={0}
              value={effect.startFrame}
              onChange={startFrame => onChange({ startFrame })}
            >
              <Label>Start frame</Label>
              <NumberField.Group>
                <NumberField.Input />
              </NumberField.Group>
            </NumberField>

            <NumberField
              maxValue={maxFrame}
              minValue={effect.startFrame}
              value={effect.endFrame}
              onChange={endFrame => onChange({ endFrame })}
            >
              <Label>End frame</Label>
              <NumberField.Group>
                <NumberField.Input />
              </NumberField.Group>
            </NumberField>
          </div>

          <div className='grid grid-cols-2 gap-3'>
            <NumberField
              minValue={1}
              value={effect.repetitions}
              onChange={repetitions => onChange({ repetitions })}
            >
              <Label>Repetitions</Label>
              <NumberField.Group>
                <NumberField.DecrementButton />
                <NumberField.Input />
                <NumberField.IncrementButton />
              </NumberField.Group>
            </NumberField>

            <NumberField
              minValue={0}
              value={effect.repetitionSpacing}
              onChange={repetitionSpacing => onChange({ repetitionSpacing })}
            >
              <Label>Rep. spacing</Label>
              <NumberField.Group>
                <NumberField.DecrementButton />
                <NumberField.Input />
                <NumberField.IncrementButton />
              </NumberField.Group>
            </NumberField>
          </div>

          <EffectTypeSection
            args={effect.args}
            items={referenceData.effects}
            selectedCode={effect.type}
            onChangeArgs={args => onChange({ args })}
            onChangeType={type => onChange({ type, args: getDefaultArgs(referenceData.effects, type) })}
          />

          <FillSection
            fill={effect.fill}
            referenceData={referenceData}
            onChange={fill => onChange({ fill })}
          />

          <EasingSection
            easing={effect.easing}
            referenceData={referenceData}
            onChange={easing => onChange({ easing })}
          />
        </Card.Content>
      )}
    </Card>
  )
}

function EffectTypeSection({
  items,
  args,
  selectedCode,
  onChangeType,
  onChangeArgs,
}: {
  items: EditorItemViewModel[]
  args: Record<string, string[]>
  selectedCode: string
  onChangeType: (type: string) => void
  onChangeArgs: (args: Record<string, string[]>) => void
}) {
  const selectedType = items.find(item => item.code === selectedCode)

  return (
    <div className='border-default flex flex-col gap-3 border-t pt-4'>
      <h3 className='text-muted text-xs font-semibold tracking-wide uppercase'>Effect type</h3>
      <TypeSelect items={items} label='Type' selectedCode={selectedCode} onChange={onChangeType} />
      <ArgFields args={args} params={selectedType?.params ?? []} onChange={onChangeArgs} />
    </div>
  )
}

function FillSection({
  fill,
  referenceData,
  onChange,
}: {
  fill: Fill
  referenceData: ReferenceDataViewModel
  onChange: (fill: Fill) => void
}) {
  const fillType = referenceData.fills.find(item => item.code === fill.type)

  return (
    <div className='border-default flex flex-col gap-3 border-t pt-4'>
      <h3 className='text-muted text-xs font-semibold tracking-wide uppercase'>Fill</h3>

      <Select
        isRequired
        placeholder='Select a blend mode'
        value={fill.blendMode}
        onChange={selected => onChange({ ...fill, blendMode: selected as Fill['blendMode'] })}
      >
        <Label>Blend mode</Label>
        <Select.Trigger>
          <Select.Value />
          <Select.Indicator />
        </Select.Trigger>
        <Select.Popover>
          <ListBox>
            {BlendModeValues.map(mode => (
              <ListBox.Item key={mode} id={mode} textValue={mode}>
                {mode}
                <ListBox.ItemIndicator />
              </ListBox.Item>
            ))}
          </ListBox>
        </Select.Popover>
      </Select>

      <TypeSelect
        items={referenceData.fills}
        label='Fill type'
        selectedCode={fill.type}
        onChange={type => onChange({ ...fill, type, args: getDefaultArgs(referenceData.fills, type) })}
      />

      <ArgFields args={fill.args} params={fillType?.params ?? []} onChange={args => onChange({ ...fill, args })} />
    </div>
  )
}

function EasingSection({
  easing,
  referenceData,
  onChange,
}: {
  easing: Easing
  referenceData: ReferenceDataViewModel
  onChange: (easing: Easing) => void
}) {
  const easingType = referenceData.easings.find(item => item.code === easing.type)

  return (
    <div className='border-default flex flex-col gap-3 border-t pt-4'>
      <h3 className='text-muted text-xs font-semibold tracking-wide uppercase'>Easing</h3>

      <TypeSelect
        items={referenceData.easings}
        label='Easing type'
        selectedCode={easing.type}
        onChange={type => onChange({ ...easing, type, args: getDefaultArgs(referenceData.easings, type) })}
      />

      <div className='grid grid-cols-2 gap-3'>
        <NumberField maxValue={100} minValue={0} value={easing.start} onChange={start => onChange({ ...easing, start })}>
          <Label>Start (%)</Label>
          <NumberField.Group>
            <NumberField.Input />
          </NumberField.Group>
        </NumberField>

        <NumberField maxValue={100} minValue={0} value={easing.end} onChange={end => onChange({ ...easing, end })}>
          <Label>End (%)</Label>
          <NumberField.Group>
            <NumberField.Input />
          </NumberField.Group>
        </NumberField>
      </div>

      <ArgFields
        args={easing.args}
        params={easingType?.params ?? []}
        onChange={args => onChange({ ...easing, args })}
      />
    </div>
  )
}

function TypeSelect({
  label,
  items,
  selectedCode,
  onChange,
}: {
  label: string
  items: EditorItemViewModel[]
  selectedCode: string
  onChange: (code: string) => void
}) {
  return (
    <Select
      isRequired
      placeholder={`Select ${label.toLowerCase()}`}
      value={selectedCode}
      onChange={selected => onChange(selected as string)}
    >
      <Label>{label}</Label>
      <Select.Trigger>
        <Select.Value />
        <Select.Indicator />
      </Select.Trigger>
      <Select.Popover>
        <ListBox>
          {items.map(item => (
            <ListBox.Item key={item.code} id={item.code} textValue={item.name}>
              {item.name}
              <ListBox.ItemIndicator />
            </ListBox.Item>
          ))}
        </ListBox>
      </Select.Popover>
    </Select>
  )
}

function getDefaultArgs(items: EditorItemViewModel[], code: string): Record<string, string[]> {
  const item = items.find(candidate => candidate.code === code)
  if (!item) {
    return {}
  }

  return Object.fromEntries(item.params.map(param => [param.code, param.defaultValue]))
}

function ArgFields({
  params,
  args,
  onChange,
}: {
  params: Param[]
  args: Record<string, string[]>
  onChange: (args: Record<string, string[]>) => void
}) {
  if (params.length === 0) {
    return null
  }

  function setArg(code: string, value: string[]) {
    onChange({ ...args, [code]: value })
  }

  return (
    <div className='flex flex-col gap-3'>
      {params.map(param => {
        const value = args[param.code] ?? param.defaultValue
        return (
          <ArgField key={param.code} param={param} value={value} onChange={next => setArg(param.code, next)} />
        )
      })}
    </div>
  )
}

function ArgField({
  param,
  value,
  onChange,
}: {
  param: Param
  value: string[]
  onChange: (value: string[]) => void
}) {
  switch (param.type) {
    case 'BOOLEAN':
      return (
        <Switch isSelected={value[0] === 'true'} onChange={isSelected => onChange([String(isSelected)])}>
          <Switch.Control>
            <Switch.Thumb />
          </Switch.Control>
          <Label>{param.displayName}</Label>
        </Switch>
      )
    case 'COLOR':
      return (
        <ColorInput label={param.displayName} value={value[0] ?? '#ffffff'} onChange={color => onChange([color])} />
      )
    case 'COLORS':
      return <MultiColorInput label={param.displayName} values={value} onChange={onChange} />
    case 'INTEGER':
      return (
        <NumberField value={Number(value[0] ?? 0)} onChange={next => onChange([String(next)])}>
          <Label>{param.displayName}</Label>
          <NumberField.Group>
            <NumberField.Input />
          </NumberField.Group>
        </NumberField>
      )
    case 'DECIMAL':
      return (
        <NumberField
          formatOptions={{ maximumFractionDigits: 4 }}
          step={0.01}
          value={Number(value[0] ?? 0)}
          onChange={next => onChange([String(next)])}
        >
          <Label>{param.displayName}</Label>
          <NumberField.Group>
            <NumberField.Input />
          </NumberField.Group>
        </NumberField>
      )
    default:
      return (
        <TextField value={value[0] ?? ''} onChange={text => onChange([text])}>
          <Label>{param.displayName}</Label>
          <Input placeholder={param.displayName} />
        </TextField>
      )
  }
}

const HEX_COLOR_PATTERN = /^#[0-9a-fA-F]{6}$/

function ColorInput({
  label,
  value,
  onChange,
}: {
  label: string
  value: string
  onChange: (value: string) => void
}) {
  return (
    <div className='flex flex-col gap-1.5'>
      <Label>{label}</Label>
      <div className='flex items-center gap-2'>
        <input
          aria-label={label}
          className='border-default h-8 w-10 shrink-0 cursor-pointer rounded border bg-transparent p-0.5'
          type='color'
          value={HEX_COLOR_PATTERN.test(value) ? value : '#000000'}
          onChange={event => onChange(event.target.value)}
        />
        <TextField aria-label={`${label} hex value`} className='flex-1' value={value} onChange={onChange}>
          <Input placeholder='#rrggbb' />
        </TextField>
      </div>
    </div>
  )
}

function MultiColorInput({
  label,
  values,
  onChange,
}: {
  label: string
  values: string[]
  onChange: (values: string[]) => void
}) {
  return (
    <div className='flex flex-col gap-2'>
      <Label>{label}</Label>
      {values.map((color, index) => (
        <div key={index} className='flex items-center gap-2'>
          <input
            aria-label={`${label} ${index + 1}`}
            className='border-default h-8 w-10 shrink-0 cursor-pointer rounded border bg-transparent p-0.5'
            type='color'
            value={HEX_COLOR_PATTERN.test(color) ? color : '#000000'}
            onChange={event => {
              const next = [...values]
              next[index] = event.target.value
              onChange(next)
            }}
          />
          <TextField
            aria-label={`${label} ${index + 1} hex value`}
            className='flex-1'
            value={color}
            onChange={text => {
              const next = [...values]
              next[index] = text
              onChange(next)
            }}
          >
            <Input placeholder='#rrggbb' />
          </TextField>
          <Button
            isIconOnly
            aria-label='Remove color'
            size='sm'
            variant='ghost'
            onPress={() => onChange(values.filter((_, candidateIndex) => candidateIndex !== index))}
          >
            <Trash2 size={14} />
          </Button>
        </div>
      ))}
      <Button size='sm' variant='secondary' onPress={() => onChange([...values, '#ffffff'])}>
        Add color
      </Button>
    </div>
  )
}

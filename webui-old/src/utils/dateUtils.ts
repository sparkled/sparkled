export const getFormattedDuration = (seconds: number) => {
  const date = new Date(0)
  date.setSeconds(Math.floor(seconds))

  if (date.getSeconds() < 3600) {
    // 1970-01-01T00:01:23.000Z
    //               ^^^^^
    return date.toISOString().substring(14, 19)
  } else {
    // 1970-01-01T00:01:23.000Z
    //            ^^^^^^^^
    return date.toISOString().substring(11, 19)
  }
}

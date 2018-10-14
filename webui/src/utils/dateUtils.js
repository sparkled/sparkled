export const getFormattedDuration = seconds => {
  const date = new Date(null);
  date.setSeconds(seconds);

  if (seconds < 3600) {
    // 1970-01-01T00:01:23.000Z
    //               ^^^^^
    return date.toISOString().substr(14, 5);
  } else {
    // 1970-01-01T00:01:23.000Z
    //            ^^^^^^^^
    return date.toISOString().substr(11, 8);
  }
};

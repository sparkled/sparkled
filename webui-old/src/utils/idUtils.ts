function uniqueId() {
  const ms = Date.now() * RANDOM_FACTOR
  const rand = Math.random() * RANDOM_FACTOR

  return (ms + rand)
    .toString(Object.keys(ID_REMAP_TABLE).length)
    .split('')
    .map(it => ID_REMAP_TABLE[it] ?? it)
    .join('')
}

/**
 * Multiplying the current millis by this factor will yield a 12-digit base 28 identifier. We can then add a random
 * number less than this factor to the timestamp to reduce the possibility of a collision.
 */
const RANDOM_FACTOR = 70000

const ID_REMAP_TABLE: Record<string, string> = {
  0: '2',
  1: '3',
  2: '4',
  3: '5',
  4: '6',
  5: '7',
  6: '8',
  7: '9',
  8: 'b',
  9: 'c',
  a: 'd',
  b: 'f',
  c: 'g',
  d: 'h',
  e: 'j',
  f: 'k',
  g: 'm',
  h: 'n',
  i: 'p',
  j: 'q',
  k: 'r',
  l: 's',
  m: 't',
  n: 'v',
  o: 'w',
  p: 'x',
  q: 'y',
  r: 'z'
}

export {
  uniqueId
}
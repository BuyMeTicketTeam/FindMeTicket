/* eslint-disable no-restricted-syntax */
export function paramsToObject(entries) {
  const result = {};
  for (const [key, value] of entries) {
    result[key] = value;
  }
  return result;
}

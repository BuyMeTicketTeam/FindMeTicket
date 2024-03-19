/* eslint-disable no-undef */
import timer from './timer';

describe('timeOut function', () => {
  it('should decrement seconds by 1', async () => {
    const { seconds, minutes } = await timer(60, 1);
    expect(seconds).toBe(59);
    expect(minutes).toBe(1);
  });

  it('should decrement minutes by 1 when seconds reach 0', async () => {
    const { seconds, minutes } = await timer(0, 1);
    expect(seconds).toBe(59);
    expect(minutes).toBe(0);
  });
});

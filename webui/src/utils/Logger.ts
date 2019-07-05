export default class Logger {
  private readonly name: string;

  constructor(name: string) {
    this.name = name;
  }

  public debug(msg: string) {
    if (process.env.NODE_ENV !== 'production') {
      console.debug(this.format(msg));
    }
  }

  public info(msg: string) {
    console.info(this.format(msg));
  }

  public warn(msg: string) {
    console.warn(this.format(msg));
  }

  public error(msg: string) {
    console.error(this.format(msg));
  }

  private format(msg: string) {
    return `[${this.name}] ${msg}`;
  }
}

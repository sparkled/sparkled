// tslint:disable:no-console
export default class Logger {
  private readonly name: string;

  constructor(name: string) {
    this.name = name;
  }

  public debug(msg: string) {
    if (process.env.NODE_ENV !== "production") {
      console.debug(this.format(msg));
    }
  }

  public info(msg: string) {
    console.info(this.format(msg));
  }

  public warn(msg: string) {
    console.warn(this.format(msg));
  }

  public error(msg: string, error?: any) {
    console.error(this.format(msg), error);
  }

  private format(msg: string) {
    return `[${this.name} ${new Date().toISOString()}] ${msg}`;
  }
}

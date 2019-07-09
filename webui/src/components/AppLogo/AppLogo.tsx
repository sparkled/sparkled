import React from "react";
import largeLogo from "./logo-large.svg";

declare interface Props {
  className: string;
}

export default (props: Props) => <img className={props.className} src={largeLogo} alt="Sparkled"/>;

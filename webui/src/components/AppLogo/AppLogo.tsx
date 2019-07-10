import React from "react";
import largeLogo from "./logo-large.svg";

declare interface Props {
  className: string;
}

const AppLogo: React.FC<Props> = (props: Props) => {
  return <img className={props.className} src={largeLogo} alt="Sparkled"/>;
};

export default AppLogo;

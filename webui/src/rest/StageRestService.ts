import axios, {AxiosResponse} from "axios";
import * as restConfig from "../config/restConfig";
import {StageViewModel} from "../types/ViewModel";
import Logger from "../utils/Logger";

const logger = new Logger("StageRestService");

type Error = [string, string];
const stageNotFoundError: Error = ["Stage not found", "The stage you're looking for no longer exists."];
const saveError: Error = ["Failed to save stage", "Please check your data and try again."];
const unexpectedError: Error = ["An unexpected error occurred", "Sorry! Please check your data and try again."];

export const loadStage = async (
  stageId: number,
  onSuccess: (stage: StageViewModel) => void,
  onFailure: (error: Error) => void
) => {
  logger.info(`Loading stage ${stageId}.`);
  try {
    const response = await axios.get(`${restConfig.ROOT_URL}/stages/${stageId}`);
    logger.info(`Loaded stage ${stageId}.`);
    onSuccess(response.data);
  } catch (error) {
    logger.error(`Failed to load stage ${stageId}.`, error);
    const status = (error.response as AxiosResponse).status;
    onFailure(status === 404 ? stageNotFoundError : unexpectedError);
  }
};

export const saveStage = async (stage: StageViewModel, onSuccess: () => void, onFailure: (error: Error) => void) => {
  logger.info(`Saving stage ${stage.id}.`);
  try {
    await axios.put(`${restConfig.ROOT_URL}/stages/${stage.id}`, stage);
    logger.info(`Saved stage ${stage.id}.`);
    onSuccess();
  } catch (error) {
    logger.error(`Failed to save stage ${stage.id}.`, error);
    onFailure(saveError);
  }
};

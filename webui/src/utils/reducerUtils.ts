export const getResponseError = (action: any) => {
  const { response } = action.payload;
  return response ? response.data : "No response received from server.";
};

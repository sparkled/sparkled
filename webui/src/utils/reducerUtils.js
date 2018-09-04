export const getResponseError = action => {
  const { response } = action.payload;
  return response ? response.data : 'No response received from server.';
};

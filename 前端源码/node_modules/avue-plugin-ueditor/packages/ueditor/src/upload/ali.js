export const getClient = (params) => {
  let { region, endpoint, accessKeyId, accessKeySecret, bucket } = params;
  //oss 基本配置
  const client = new window.OSS({ region, endpoint, accessKeyId, accessKeySecret, bucket })
  return client;
}
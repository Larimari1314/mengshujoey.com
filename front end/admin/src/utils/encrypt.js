import JSEncrypt from 'jsencrypt'

const privateKey =
  'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMFPa+v52FkSUXvcUnrGI/XzW3EpZRI0s9BCWJ3oNQmEYA5luWW5p8h0uadTIoTyYweFPdH4hveyxlwmS7oefvbIdiP+o+QIYW/R4Wjsb4Yl8MhR4PJqUE3RCy6IT9fM8ckG4kN9ECs6Ja8fQFc6/mSl5dJczzJO3k1rWMBhKJD/AgMBAAECgYEAucMakH9dWeryhrYoRHcXo4giPVJsH9ypVt4KzmOQY/7jV7KFQK3x//27UoHfUCak51sxFw9ek7UmTPM4HjikA9LkYeE7S381b4QRvFuf3L6IbMP3ywJnJ8pPr2l5SqQ00W+oKv+w/VmEsyUHr+k4Z+4ik+FheTkVWp566WbqFsECQQDjYaMcaKw3j2Zecl8T6eUe7fdaRMIzp/gcpPMfT/9rDzIQk+7ORvm1NI9AUmFv/FAlfpuAMrdL2n7p9uznWb7RAkEA2aP934kbXg5bdV0R313MrL+7WTK/qdcYxATUbMsMuWWQBoS5irrt80WCZbG48hpocJavLNjbtrjmUX3CuJBmzwJAOJg8uP10n/+ZQzjEYXh+BszEHDuw+pp8LuT/fnOy5zrJA0dO0RjpXijO3vuiNPVgHXT9z1LQPJkNrb5ACPVVgQJBALPeb4uV0bNrJDUb5RB4ghZnIxv18CcaqNIft7vuGCcFBAIPIRTBprR+RuVq+xHDt3sNXdsvom4h49+Hky1b0ksCQBBwUtVaqH6ztCtwUF1j2c/Zcrt5P/uN7IHAd44K0gIJc1+Csr3qPG+G2yoqRM8KVqLI8Z2ZYn9c+AvEE+L9OQY='

/**
 * @description RSA加密
 * @param publicKey
 * @param data
 * @returns {Promise<{param: PromiseLike<ArrayBuffer>}|*>}
 */
export function encryptedData(publicKey, data) {
  if (publicKey === '') {
    return data
  }
  const encrypt = new JSEncrypt()
  encrypt.setPublicKey(publicKey)
  data = encrypt.encrypt(JSON.stringify(data))
  return data
}

/**
 * @author https://vue-admin-beautiful.com （不想保留author可删除）
 * @description RSA解密
 * @param data
 * @returns {PromiseLike<ArrayBuffer>}
 */
export function decryptedData(data) {
  const decrypt = new JSEncrypt()
  decrypt.setPrivateKey(
    `-----BEGIN RSA PRIVATE KEY-----${privateKey}-----END RSA PRIVATE KEY-----`
  )
  data = decrypt.decrypt(JSON.stringify(data))
  return data
}
//加密
export function encryptoPSW(str, xor, hex) {
  if (
    typeof str !== 'string' ||
    typeof xor !== 'number' ||
    typeof hex !== 'number'
  ) {
    return
  }
  let resultList = []
  hex = hex <= 25 ? hex : hex % 25

  for (let i = 0; i < str.length; i++) {
    // 提取字符串每个字符的ascll码
    let charCode = str.charCodeAt(i)
    // 进行异或加密
    charCode = charCode ^ xor
    // 异或加密后的字符转成 hex 位数的字符串
    charCode = charCode.toString(hex)
    resultList.push(charCode)
  }
  let splitStr = String.fromCharCode(hex + 97)
  return resultList.join(splitStr)
}

//解密
export function decrypto(str, xor, hex) {
  if (
    typeof str !== 'string' ||
    typeof xor !== 'number' ||
    typeof hex !== 'number'
  ) {
    return
  }
  let strCharList = []
  let resultList = []
  hex = hex <= 25 ? hex : hex % 25
  // 解析出分割字符
  let splitStr = String.fromCharCode(hex + 97)
  // 分割出加密字符串的加密后的每个字符
  strCharList = str.split(splitStr)

  for (let i = 0; i < strCharList.length; i++) {
    // 将加密后的每个字符转成加密后的ascll码
    let charCode = parseInt(strCharList[i], hex)
    // 异或解密出原字符的ascll码
    charCode = charCode ^ xor
    let strChar = String.fromCharCode(charCode)
    resultList.push(strChar)
  }
  return resultList.join('')
}

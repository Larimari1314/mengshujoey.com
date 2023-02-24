import CryptoJS from 'crypto-js'
import base from 'qs/lib/utils'
import { Base64 } from 'jsencrypt/lib/lib/asn1js/base64'

/**
 * @description ECB加密
 * @param publicKey
 * @param data
 * @returns {Promise<{param: PromiseLike<ArrayBuffer>}|*>}
 */
export function encryptedData(key, data) {
  try {
    var encryptedData = CryptoJS.AES.encrypt(
      plaintText,
      CryptoJS.enc.Utf8.parse(key),
      {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7,
      }
    )
    return encryptedData
  } catch (e) {
    //删除页面存储的加密信息
    localStorage.removeItem('privateKey')
    this.$message.error('数据更新失败，请刷新界面！')
  }
}
/**
 * @description ECB解密
 * @param key
 * @param data
 * @returns {PromiseLike<ArrayBuffer>}
 */
export function decryptedData(key, data) {
  try {
    var decryptedData = CryptoJS.AES.decrypt(
      data,
      CryptoJS.enc.Utf8.parse(key),
      {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7,
      }
    )
    var decryptedStr = decryptedData.toString(CryptoJS.enc.Utf8)
    return JSON.parse(decryptedStr)
  } catch (e) {
    //删除页面存储的加密信息
    localStorage.removeItem('privateKey')
    this.$message.error('数据更新失败，请刷新界面！')
  }
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
/**
 * 16进制转byte数组
 */
function hexToBytes(hex) {
  let bytes = []
  for (let c = 0; c < hex.length; c += 2)
    bytes.push(parseInt(hex.substr(c, 2), 16))
  return bytes
}

/**
 * byte数组转16进制
 * @param bytes
 * @returns {string}
 */
function bytesToHex(bytes) {
  let hex = []
  for (let i = 0; i < bytes.length; i++) {
    hex.push((bytes[i] >>> 4).toString(16))
    hex.push((bytes[i] & 0xf).toString(16))
  }
  return hex.join('')
}

/**
 * base64转btye数组
 * @param base64
 * @returns {Uint8Array}
 */
function base64ToArrayBuffer(base64) {
  let binary_string = window.atob(base64)
  let len = binary_string.length
  let bytes = new Uint8Array(len)
  for (let i = 0; i < len; i++) {
    bytes[i] = binary_string.charCodeAt(i)
  }

  return bytes
}

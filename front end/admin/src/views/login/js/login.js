import { isPassword } from '@/utils/validate'
import { adminLogin, getPublicKeyLogin } from '@/api/assessmentItems'
import { decrypto, encryptedData, encryptoPSW } from '@/utils/encrypt'
import dragVerifyImgRotate from 'vue-drag-verify-img-rotate'

import Vue from 'vue'
export default {
  name: '',
  components: {
    dragVerifyImgRotate,
  },
  directives: {
    focus: {
      inserted(el) {
        el.querySelector('input').focus()
      },
    },
  },
  data() {
    const validateusername = (rule, value, callback) => {
      if ('' == value) {
        callback(new Error('登陆邮件不能为空'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (!isPassword(value)) {
        callback(new Error('请按要求输入6-18位由字母、数字、下划线构成的密码'))
      } else {
        callback()
      }
    }
    return {
      isPassing: false,
      dialogVisible: false,
      publicKey: undefined,
      nodeEnv: process.env.NODE_ENV,
      title: this.$baseTitle,
      form: {
        username: '',
        password: '',
      },
      rules: {
        username: [
          {
            required: true,
            trigger: 'blur',
            validator: validateusername,
          },
          {
            pattern: /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/,
            message: '邮件格式错误',
            trigger: 'blur',
          },
        ],
        password: [
          {
            required: true,
            trigger: 'blur',
            validator: validatePassword,
          },
          {
            pattern: /^[a-zA-Z0-9_-]{6,18}$/,
            trigger: 'blur',
          },
        ],
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined,
    }
  },
  watch: {
    $route: {
      handler(route) {
        this.redirect = (route.query && route.query.redirect) || '/'
      },
      immediate: true,
    },
  },
  created() {
    document.body.style.overflow = 'hidden'
  },
  beforeDestroy() {
    document.body.style.overflow = 'auto'
  },
  mounted() {
    this.publicKey = decrypto(localStorage.getItem('mengshuPubey'), 520, 21)
    var that = this
    if (this.publicKey === undefined) {
      //如果私钥为空的话，访问连接获取私钥地址，将其存储时间和公钥地址存储到界面中
      //请求服务器获取登录密钥
      getPublicKeyLogin().then((req) => {
        //公钥地址
        this.publicKey = req.data
        var publickKey = {
          publickKey: req.data,
          date: new Date(),
        }
        localStorage.setItem(
          'mengshuPubey',
          encryptoPSW(JSON.stringify(publickKey), 520, 21)
        )
      })
    } else {
      //取出公钥和存储时间的信息
      var publicKeys = JSON.parse(this.publicKey)
      //判断存储公钥时间是否超出当前月份15
      var publicDate = new Date(publicKeys.date)
      var now = new Date()
      //经典问题两个日期间是否夹另一个日期
      if (
        publicDate.getMonth() < now.getMonth() ||
        publicDate.getDay() < 15 < now.getDay()
      ) {
        //更新
        //说明密钥过期，清除密钥重新进行判断
        localStorage.removeItem('mengshuPubey')
        location.reload()
      } else {
        //说明新的数据
        this.publicKey = publicKeys['publickKey']
      }
    }
  },
  methods: {
    reimg() {
      this.$refs.dragVerify.reset()
    },
    handleClose() {
      this.dialogVisible = false
    },
    onSuccess() {
      let that = this
      let sleep = function (time) {
        setTimeout(() => {
          that.handleLogin()
          that.handleClose()
          that.$refs.dragVerify.reset()
        }, time)
      }
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)',
      })
      setTimeout(() => {
        loading.close()
      }, 2000)
      sleep(1900)
    },
    handlePassword() {
      this.passwordType === 'password'
        ? (this.passwordType = '')
        : (this.passwordType = 'password')
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    verify() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          this.dialogVisible = true
        }
      })
    },
    handleLogin() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          let _this = this
          if (this.publicKey === '') {
            Vue.prototype.$baseMessage('数据加载失败', 'error')
          } else {
            this.loading = true
            //将用户输入数据封装到表单中
            var forms = {
              LoginCredentials: encryptedData(this.publicKey, {
                loginEmail: this.form.username,
                password: this.form.password,
              }),
            }
            //加密数据result
            adminLogin(forms)
              .then(function (req) {
                if (req.data.code === '200') {
                  sessionStorage.setItem(
                    'JSESSIONID',
                    req.data.msgParams.sessionId
                  )
                  sessionStorage.setItem('accessTokens', req.headers['token'])
                  //权限信息加载
                  sessionStorage.setItem(
                    'permissions',
                    encryptoPSW(
                      JSON.stringify(req.data.data.permissions),
                      125,
                      20
                    )
                  )
                  req.data.data.permissions = undefined
                  //将用户信息存储到页面域中
                  //转码操作，数据进一步加密
                  //解密：decrypto(JSON.stringify(req.data.result[1]),125,20);
                  sessionStorage.setItem(
                    'userInformation',
                    encryptoPSW(JSON.stringify(req.data.data), 125, 20)
                  )
                  location.reload()
                  //跳转首页
                  _this.loading = false
                }
              })
              .catch((res) => {
                if (res.response !== undefined) {
                  if (res.response.data.code === '500') {
                    this.$message({
                      type: 'error',
                      message:
                        '登录失败，请检查账号密码是否输入错误或账号已被禁用',
                    })
                  }
                }
                this.loading = false
              })
          }
        } else {
          return false
        }
      })
    },
  },
}

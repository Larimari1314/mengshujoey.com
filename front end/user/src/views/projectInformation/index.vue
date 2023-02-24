<template>
  <div v-if="'horizontal' === layout" class="index-container">
    <pc-index-project ref="pdIndex" />
  </div>
  <div v-else class="index-container">
    <cell-phone-index-project ref="cellPhoneIndex" />
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import pcIndexProject from '@/views/projectInformation/components/PcIndex'
  import cellPhoneIndexProject from '@/views/projectInformation/components/cellPhoneIndex'
  import { individualClicks } from '@/api/mainIndex'
  export default {
    name: 'ProjectIndex',
    components: {
      pcIndexProject,
      cellPhoneIndexProject,
    },
    data() {
      return {
        projectId: undefined,
      }
    },
    computed: {
      ...mapGetters({
        layout: 'settings/layout',
      }),
    },
    mounted() {
      let projectId = this.$route.params.id
      let item = localStorage.getItem('historicalClicks')
      if (item == null) {
        this.searchQuestion()
      } else {
        let parse = JSON.parse(item)
        let count = false
        parse.forEach((itee) => {
          if (projectId == itee) {
            count = true
          }
        })
        if (!count) {
          this.searchQuestion()
        }
      }
    },
    methods: {
      searchQuestion() {
        let projectId = this.$route.params.id
        individualClicks(projectId).then((res) => {
          if (res.data.code === '200') {
            let item = localStorage.getItem('historicalClicks')
            if (item == null) {
              let data = []
              data.push(projectId)
              localStorage.setItem('historicalClicks', JSON.stringify(data))
            } else {
              let parse = JSON.parse(item)
              parse.push(projectId)
              localStorage.setItem('historicalClicks', JSON.stringify(parse))
            }
          }
        })
      },
    },
  }
</script>

<style scoped></style>

import request from '@/utils/request'

// 查询陕西PICC请求接口列表
export function listShanxipicc(query) {
  return request({
    url: '/system/shanxipicc/list',
    method: 'get',
    params: query
  })
}

// 查询陕西PICC请求接口详细
export function getShanxipicc(id) {
  return request({
    url: '/system/shanxipicc/' + id,
    method: 'get'
  })
}

// 新增陕西PICC请求接口
export function addShanxipicc(data) {
  return request({
    url: '/system/shanxipicc',
    method: 'post',
    data: data
  })
}

// 修改陕西PICC请求接口
export function updateShanxipicc(data) {
  return request({
    url: '/system/shanxipicc',
    method: 'put',
    data: data
  })
}

// 删除陕西PICC请求接口
export function delShanxipicc(id) {
  return request({
    url: '/system/shanxipicc/' + id,
    method: 'delete'
  })
}

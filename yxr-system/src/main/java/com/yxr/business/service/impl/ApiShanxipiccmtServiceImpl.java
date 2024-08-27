package com.yxr.business.service.impl;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxr.business.domain.ApiShanxipiccdt;
import com.yxr.business.domain.ApiShanxipiccmt;
import com.yxr.business.mapper.ApiShanxipiccdtMapper;
import com.yxr.business.mapper.ApiShanxipiccmtMapper;
import com.yxr.business.service.IApiShanxipiccmtService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxr.common.annotation.DataSource;
import com.yxr.common.core.domain.AjaxResult;
import com.yxr.common.enums.DataSourceType;
import com.yxr.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * shanxi接口汇总表 服务实现类
 * </p>
 *
 * @author GS
 * @since 2024-08-22
 */
@Service
@DataSource(DataSourceType.SLAVE)
public class ApiShanxipiccmtServiceImpl extends ServiceImpl<ApiShanxipiccmtMapper, ApiShanxipiccmt> implements IApiShanxipiccmtService {

    @Autowired
    ApiShanxipiccmtMapper apiShanxipiccmtMapper;


    @Autowired
    ApiShanxipiccdtMapper apiShanxipiccdtMapper;

    @Transactional
    @Override
    public AjaxResult addApiShanxipicc(JSONObject json) {

        ApiShanxipiccmt apiShanxipiccmt = new ApiShanxipiccmt();
        ApiShanxipiccdt apiShanxipiccdt = new ApiShanxipiccdt();
        QueryWrapper<ApiShanxipiccmt> wrapper = new QueryWrapper<>();
        wrapper.eq("feedetl_no",json.getStr("feedetlNo"));//根据交易流水号看
        List<ApiShanxipiccmt> apiShanxipiccmtList =  apiShanxipiccmtMapper.selectList(wrapper);
        if(apiShanxipiccmtList.size()>0){
            //如果有数据,返回给他们 已有数据 请勿重复提交
            //iApiShanxipiccmtService.updateById(apiShanxipiccmtList.get(0));
            return  AjaxResult.error("请勿重复提交订单");
        }
        Long billNo = apiShanxipiccmtMapper.selectBillNo();
        apiShanxipiccmt.setBillNo(billNo);
        apiShanxipiccmt.setFixmedinsCode(json.getStr("fixmedinsCode"));
        apiShanxipiccmt.setFeedetlNo(json.getStr("feedetlNo"));
        apiShanxipiccmt.setMedfeeSumamt(json.getBigDecimal("medfeeSumamt").toString());
        apiShanxipiccmt.setHifpPay(json.getStr("hifpPay"));
        apiShanxipiccmt.setAcctPay(json.getStr("acctPay"));
        apiShanxipiccmt.setPsnCashpay(json.getStr("psnCashPay"));
        apiShanxipiccmt.setInfoTime(json.getStr("infoTime"));
        apiShanxipiccmt.setPayType(json.getStr("feedetlNo"));
        apiShanxipiccmt.setCertNo(json.getJSONObject("setlinfo").getStr("certno"));
        apiShanxipiccmt.setPsnName(json.getJSONObject("setlinfo").getStr("psn_name"));
        //退单状态 1,正常 2，已退单
        apiShanxipiccmt.setStatus("1");
        apiShanxipiccmt.setRequest(json.toString());
        apiShanxipiccmt.setCreateTime(DateUtils.getNowDate());
        /*收银台是否已提帐 Y 已提帐 N 未提帐*/
        apiShanxipiccmt.setIsDone("N");

        apiShanxipiccmtMapper.insert(apiShanxipiccmt);

        JSONArray drugList = json.getJSONArray("drugList");
        for (Object item : drugList){
            JSONObject drug = new JSONObject(item);
            apiShanxipiccdt.setBillNo(billNo);
            apiShanxipiccdt.setInsuitemcode(drug.getStr("insuItemCode"));
            apiShanxipiccdt.setInsuitemname(drug.getStr("insuItemName"));
            apiShanxipiccdt.setItemnum(drug.getStr("itemNum"));
            apiShanxipiccdt.setPrice(drug.getStr("price"));
            apiShanxipiccdtMapper.insert(apiShanxipiccdt);
        }

        return  AjaxResult.success();
    }

    @Override
    public Long selectBillNo() {

        return apiShanxipiccmtMapper.selectBillNo();
    }

    @Transactional
    @Override
    public AjaxResult updateByfeedetlNo(JSONObject json) {
        //{
        //	"drugStoreNo":"P6103020202",
        //	"feedetlNo":"fa1603c6e3b3415484e016df30796ceb"
        //}
        QueryWrapper<ApiShanxipiccmt> wrapper = new QueryWrapper<>();
        wrapper.eq("feedetl_no",json.getStr("feedetlNo"));//根据交易流水号看
        List<ApiShanxipiccmt> apiShanxipiccmtList =  apiShanxipiccmtMapper.selectList(wrapper);
        if(apiShanxipiccmtList.size() ==  0){
            //如果有数据,返回给他们 已有数据 请勿重复提交
            //iApiShanxipiccmtService.updateById(apiShanxipiccmtList.get(0));
            return  AjaxResult.error("未找到对应的订单");
        }

         /*ApiShanxipiccmt apiShanxipiccmt = new ApiShanxipiccmt();
         apiShanxipiccmt.setStatus("2");
         apiShanxipiccmt.setFeedetlNo(json.getStr("feedetlNo"));
         apiShanxipiccmtMapper.updateByfeedetlNo(apiShanxipiccmt);*/
        //不更新原来订单，新增一笔订单
        ApiShanxipiccmt apiShanxipiccmt = apiShanxipiccmtList.get(0);
        Long billNo = apiShanxipiccmtMapper.selectBillNo();

        //先入明细
        ApiShanxipiccdt apiShanxipiccdt = new ApiShanxipiccdt();
        JSONArray drugList = new JSONObject(apiShanxipiccmt.getRequest()).getJSONArray("drugList");
        for (Object item : drugList){
            JSONObject drug = new JSONObject(item);
            apiShanxipiccdt.setBillNo(billNo);
            apiShanxipiccdt.setInsuitemcode(drug.getStr("insuItemCode"));
            apiShanxipiccdt.setInsuitemname(drug.getStr("insuItemName"));
            apiShanxipiccdt.setItemnum("-"+drug.getStr("itemNum"));
            apiShanxipiccdt.setPrice(drug.getStr("price"));
            apiShanxipiccdtMapper.insert(apiShanxipiccdt);
        }


        //再入总帐，金额变为负
        apiShanxipiccmt.setBillNo(billNo);
        apiShanxipiccmt.setIsDone("N");
        apiShanxipiccmt.setRequest("");

        BigDecimal acctPay =  new BigDecimal(apiShanxipiccmt.getAcctPay());
        if(acctPay.compareTo(BigDecimal.ZERO) != 0){
            apiShanxipiccmt.setAcctPay(acctPay.negate().toString());
        }

        apiShanxipiccmt.setMedfeeSumamt(new BigDecimal(apiShanxipiccmt.getMedfeeSumamt()).negate().toString());

        apiShanxipiccmt.setHifpPay("-"+apiShanxipiccmt.getHifpPay());


        BigDecimal cashpay =  new BigDecimal(apiShanxipiccmt.getPsnCashpay());
        if(cashpay.compareTo(BigDecimal.ZERO) != 0){
            apiShanxipiccmt.setPsnCashpay(cashpay.negate().toString());
        }

        apiShanxipiccmtMapper.insert(apiShanxipiccmt);
        return AjaxResult.success();
    }
}

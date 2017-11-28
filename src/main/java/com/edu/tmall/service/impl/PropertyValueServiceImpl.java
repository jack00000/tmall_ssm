package com.edu.tmall.service.impl;

import com.edu.tmall.mapper.PropertyValueMapper;
import com.edu.tmall.pojo.Product;
import com.edu.tmall.pojo.Property;
import com.edu.tmall.pojo.PropertyValue;
import com.edu.tmall.pojo.PropertyValueExample;
import com.edu.tmall.service.PropertyService;
import com.edu.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 何腾飞 on 17/11/28.
 */
@Service
public class PropertyValueServiceImpl implements PropertyValueService {

    @Autowired
    private PropertyValueMapper propertyValueMapper;

    @Autowired
    private PropertyService propertyService;

    /**
     * 1.初始化PropertyValue,对于PropertyValue的管理，没有增加，只有修改。 所以需要通过初始化来进行自动地增加，以便于后面的修改
     * 2.首先根据产品获取分类，然后获取这个分类下的所有属性集合
     * 3.然后用属性和id产品id去查询，看看这个属性和这个产品是否已经存在属性值了。如果不存在，就创建一个属性值并设置其属性和产品，接着插入到数据库中。
     * @param p
     */
    @Override
    public void init(Product p) {
        List<Property> pts = propertyService.list(p.getCid());
        for (Property pt : pts) {
            PropertyValue pv = get(pt.getId(), p.getId());
            if (pv==null) {
                pv = new PropertyValue();
                pv.setPid(p.getId());
                pv.setPtid(pt.getId());
                propertyValueMapper.insert(pv);
            }
        }
    }

    /**
     * 更新
     * @param pv
     */
    @Override
    public void update(PropertyValue pv) {
        propertyValueMapper.updateByPrimaryKeySelective(pv);
    }

    /**
     * 根据属性id和产品id获取PropertyValue对象
     * @param ptid
     * @param pid
     * @return
     */
    @Override
    public PropertyValue get(int ptid, int pid) {
        PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPidEqualTo(pid).andPtidEqualTo(ptid);
        List<PropertyValue> ptvs = propertyValueMapper.selectByExample(example);
        if (ptvs.isEmpty())
            return null;
        return ptvs.get(0);
    }

    /**
     * 根据产品id获取所有的属性值
     * @param pid
     * @return
     */
    @Override
    public List<PropertyValue> list(int pid) {
        PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPidEqualTo(pid);
        List<PropertyValue> ptvList = propertyValueMapper.selectByExample(example);
        for (PropertyValue pv : ptvList) {
            Property property = propertyService.get(pv.getPtid());
            pv.setProperty(property);
        }
        return ptvList;
    }
}

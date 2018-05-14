package com.nguyen.wechat.model;

import com.nguyen.wechat.utils.DateUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@DynamicInsert
@DynamicUpdate
@MappedSuperclass
@EntityListeners({EntityModel.DateEntityListener.class})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class EntityModel implements Serializable{
    private static final long serialVersionUID = 7191312967943387849L;

    /** 主键ID **/
    @Id
    @Column(name = "id", length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    public String id;

    /** 创建时间 **/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    public Date createTime;

    /** 更新时间 **/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    public Date updateTime;


    public static class DateEntityListener {
        /** 当持久化对象更新时，在更新前就会执行这个函数，用于自动更新修改日期字段 */
        @PreUpdate
        public void onPreUpdate(Object o) {
            if (o instanceof EntityModel) {
                Date now = DateUtils.now();
                EntityModel em = (EntityModel) o;
                em.updateTime = now;
                if(null == em.createTime) {
                    em.createTime = now;
                }
            }
        }

        /** 当保存一个entity对象时，在保存之前会执行这个函数，用于自动添加创建日期 */
        @PrePersist
        public void onPrePersist(Object o) {
            if (o instanceof EntityModel) {
                Date currentDate = DateUtils.now();
                ((EntityModel) o).createTime = currentDate;
                ((EntityModel) o).updateTime = currentDate;
            }
        }
    }
}

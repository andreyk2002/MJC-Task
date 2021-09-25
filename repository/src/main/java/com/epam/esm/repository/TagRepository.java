package com.epam.esm.repository;

import com.epam.esm.entity.GiftTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface TagRepository extends JpaRepository<GiftTag, Long> {

    @Query(value = "select t.name AS name, ct.tag_id AS id, oc.certificate_id, oc.order_id, count(tag_id)" +
            " FROM tag t" +
            " JOIN certificate_tag ct" +
            " ON t.id = ct.id" +
            " JOIN order_certificate oc" +
            " ON ct.certificate_id = oc.certificate_id" +
            " JOIN user_order uo" +
            " ON uo.id = oc.order_id" +
            " where uo.user_id = (" +
            " select uo.user_id from user_order uo" +
            " group by uo.user_id order by sum(uo.order_price) desc limit 1" +
            ") group by tag_id order by count(tag_id) desc limit 1", nativeQuery = true)
    GiftTag getTopUserTopTag();


}

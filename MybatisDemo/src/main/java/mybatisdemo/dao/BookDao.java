package mybatisdemo.dao;

import mybatisdemo.domain.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BookDao {
    @Update("update tbl_book set type=#{type},name=#{name},description=#{description} where id=#{id};")
    int update(Book book);
}

package mybatisdemo.service;

import mybatisdemo.domain.Book;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BookService {
    boolean update(Book book);
}

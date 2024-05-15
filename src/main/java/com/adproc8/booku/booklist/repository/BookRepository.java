package com.adproc8.booku.booklist.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.adproc8.booku.booklist.model.Book;

public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {
    List<Book> findAll(Specification<Book> spec);
    List<Book> findAll(Specification<Book> spec, Sort sort);

    class BookSpecifications {
        public static Specification<Book> authorIs(String author) {
            return (root, query, builder) -> {
                return builder.equal(root.get("author"), author);
            };
        }

        public static Specification<Book> titleIs(String title) {
            return (root, query, builder) -> {
                return builder.equal(root.get("title"), title);
            };
        }
    }
}

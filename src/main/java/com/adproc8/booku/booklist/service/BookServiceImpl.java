package com.adproc8.booku.booklist.service;

import static com.adproc8.booku.booklist.repository.BookRepository.BookSpecifications.idIn;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.adproc8.booku.booklist.dto.RatingByBookIdDto;
import com.adproc8.booku.booklist.model.Book;
import com.adproc8.booku.booklist.repository.BookRepository;

@Service
class BookServiceImpl implements BookService {

    private static final ParameterizedTypeReference<List<RatingByBookIdDto>> REVIEW_LIST_TYPE =
            new ParameterizedTypeReference<List<RatingByBookIdDto>>() {};
    private static final String GET_RATINGS_GROUP_BY_BOOK_ID_PATH =
            "/api/reviews/rating/by/book-id";

    private final String reviewHost;

    private final BookRepository bookRepository;
    private final RestClient restClient;

    @Autowired
    BookServiceImpl(
        BookRepository bookRepository, RestClient restClient,
        @Value("${api.review-host}") String reviewHost)
    {
        this.bookRepository = bookRepository;
        this.restClient = restClient;
        this.reviewHost = reviewHost;
    }

    private List<Book> findAllOrderByRating(Direction direction)
    throws RestClientException
    {
        List<RatingByBookIdDto> reviews = restClient.get()
                .uri(reviewHost,
                        builder -> builder
                                .path(GET_RATINGS_GROUP_BY_BOOK_ID_PATH)
                                .queryParam("orderBy", direction)
                                .build())
                .retrieve()
                .toEntity(REVIEW_LIST_TYPE)
                .getBody();

        List<UUID> bookIds = reviews.stream()
                .map(RatingByBookIdDto::getBookId)
                .toList();

        return findAllById(bookIds);
    }

    private List<Book> findAllOrderByRating(Specification<Book> spec, Direction direction)
    throws RestClientException
    {
        List<RatingByBookIdDto> reviews = restClient.get()
                .uri(reviewHost,
                        builder -> builder
                                .path(GET_RATINGS_GROUP_BY_BOOK_ID_PATH)
                                .queryParam("orderBy", direction)
                                .build())
                .retrieve()
                .toEntity(REVIEW_LIST_TYPE)
                .getBody();

        Set<UUID> bookIds = reviews.stream()
                .map(RatingByBookIdDto::getBookId)
                .collect(Collectors.toSet());

        return findAllById(bookIds, spec);
    }

    public Book save(Book book) throws DataAccessException {
        return bookRepository.save(book);
    }

    public List<Book> saveAll(List<Book> books) throws DataAccessException {
        return bookRepository.saveAll(books);
    }

    public Optional<Book> findById(UUID bookId) {
        return bookRepository.findById(bookId);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findAll(Sort sort) throws RestClientException {
        List<Book> books;

        Order order = sort.get()
                .findFirst()
                .get();

        String property = order.getProperty();
        Direction direction = order.getDirection();

        if (property.equals("rating")) {
            books = findAllOrderByRating(direction);
        } else {
            books = bookRepository.findAll(sort);
        }

        return books;
    }

    public List<Book> findAll(Specification<Book> spec) {
        return bookRepository.findAll(spec);
    }

    public List<Book> findAll(Specification<Book> spec, Sort sort)
    throws RestClientException
    {
        List<Book> books;

        Order order = sort.get()
                .findFirst()
                .get();

        String property = order.getProperty();
        Direction direction = order.getDirection();

        if (property.equals("rating")) {
            books = findAllOrderByRating(spec, direction);
        } else {
            books = bookRepository.findAll(spec, sort);
        }

        return books;
    }

    public List<Book> findAllById(Iterable<UUID> ids) {
        return bookRepository.findAllById(ids);
    }

    public List<Book> findAllById(Set<UUID> ids, Specification<Book> spec) {
        spec = spec.and(idIn(ids));
        return bookRepository.findAll(spec);
    }

    public void deleteById(UUID bookId) {
        bookRepository.deleteById(bookId);
    }

    public void deleteAll(List<Book> books) {
        bookRepository.deleteAllInBatch(books);
    }
}

package com.adproc8.booku.booklist.service;

import static com.adproc8.booku.booklist.repository.BookRepository.BookSpecifications.idIn;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
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

    private List<RatingByBookIdDto> getReviewDtos() {
        List<RatingByBookIdDto> reviewDtos = restClient.get()
                .uri(reviewHost,
                        builder -> builder
                                .path(GET_RATINGS_GROUP_BY_BOOK_ID_PATH)
                                .build())
                .retrieve()
                .toEntity(REVIEW_LIST_TYPE)
                .getBody();

        return reviewDtos;
    }

    private List<RatingByBookIdDto> getReviewDtos(Sort sort) {
        Order order = sort.get()
                .findFirst()
                .get();

        List<RatingByBookIdDto> reviewDtos = restClient.get()
                .uri(reviewHost,
                        builder -> builder
                                .path(GET_RATINGS_GROUP_BY_BOOK_ID_PATH)
                                .queryParam("orderBy", order.getDirection())
                                .build())
                .retrieve()
                .toEntity(REVIEW_LIST_TYPE)
                .getBody();

        return reviewDtos;
    }

    private List<Book> updateBookRatings(
        List<RatingByBookIdDto> reviewDtos,
        Map<UUID, Book> bookIdToBookMap)
    {
        for (RatingByBookIdDto dto : reviewDtos) {
            UUID bookId = dto.getBookId();
            float averageRating = dto.getAverageRating();

            Book book = bookIdToBookMap.get(bookId);
            book.setRating(averageRating);
        }

        List<Book> books = bookIdToBookMap.values()
                .stream()
                .toList();

        bookRepository.saveAll(books);

        return books;
    }

    private List<Book> findAllOrderByRating(Sort sort)
    throws RestClientException
    {
        List<RatingByBookIdDto> reviewDtos = getReviewDtos(sort);

        Map<UUID, Book> bookIdToBookMap = findAll().stream()
                .collect(Collectors.toMap(Book::getId, Function.identity()));

        updateBookRatings(reviewDtos, bookIdToBookMap);

        return bookRepository.findAll(sort);
    }

    private List<Book> findAllOrderByRating(Specification<Book> spec, Sort sort)
    throws RestClientException
    {
        List<RatingByBookIdDto> reviewDtos = getReviewDtos(sort);

        Map<UUID, Book> bookIdToBookMap = findAll(spec).stream()
                .collect(Collectors.toMap(Book::getId, Function.identity()));

        updateBookRatings(reviewDtos, bookIdToBookMap);

        return bookRepository.findAll(spec, sort);
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
        List<RatingByBookIdDto> reviewDtos = getReviewDtos();
        Map<UUID, Book> bookIdToBookMap = bookRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Book::getId, Function.identity()));
        List<Book> books = updateBookRatings(reviewDtos, bookIdToBookMap);
        return books;
    }

    public List<Book> findAll(Sort sort) throws RestClientException {
        List<Book> books;

        Order order = sort.get()
                .findFirst()
                .get();

        String property = order.getProperty();

        if (property.equals("rating")) {
            books = findAllOrderByRating(sort);
        } else {
            books = bookRepository.findAll(sort);
        }

        return books;
    }

    public List<Book> findAll(Specification<Book> spec) {
        List<RatingByBookIdDto> reviewDtos = getReviewDtos();
        Map<UUID, Book> bookIdToBookMap = bookRepository.findAll(spec)
                .stream()
                .collect(Collectors.toMap(Book::getId, Function.identity()));
        List<Book> books = updateBookRatings(reviewDtos, bookIdToBookMap);
        return books;
    }

    public List<Book> findAll(Specification<Book> spec, Sort sort)
    throws RestClientException
    {
        List<Book> books;

        Order order = sort.get()
                .findFirst()
                .get();
        String property = order.getProperty();

        if (property.equals("rating")) {
            books = findAllOrderByRating(spec, sort);
        } else {
            books = bookRepository.findAll(spec, sort);
        }

        return books;
    }

    public List<Book> findAllById(Iterable<UUID> ids) {
        List<RatingByBookIdDto> reviewDtos = getReviewDtos();
        Map<UUID, Book> bookIdToBookMap = bookRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(Book::getId, Function.identity()));
        List<Book> books = updateBookRatings(reviewDtos, bookIdToBookMap);
        return books;
    }

    public List<Book> findAllById(Set<UUID> ids, Specification<Book> spec) {
        spec = spec.and(idIn(ids));
        List<RatingByBookIdDto> reviewDtos = getReviewDtos();
        Map<UUID, Book> bookIdToBookMap = bookRepository.findAll(spec)
                .stream()
                .collect(Collectors.toMap(Book::getId, Function.identity()));
        List<Book> books = updateBookRatings(reviewDtos, bookIdToBookMap);
        return books;
    }

    public void deleteById(UUID bookId) {
        bookRepository.deleteById(bookId);
    }

    public void deleteAll(List<Book> books) {
        bookRepository.deleteAllInBatch(books);
    }
}

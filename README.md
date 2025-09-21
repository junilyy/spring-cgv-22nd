# spring-cgv-22nd
CEOS 22기 백엔드 스터디 - CGV 클론 코딩 프로젝트

---

## ERD
<img width="972" height="653" alt="Image" src="https://github.com/user-attachments/assets/d9d5b112-f6b5-4f02-ab72-59f028b296c6" />

- ### Movie
  <img width="553" height="333" alt="Image" src="https://github.com/user-attachments/assets/6ec54307-f0d2-4887-bd5d-5e1a1b534de2" />

  Movie(제목, 장르 러닝타임 등 영화의 기본 정보) -> 리뷰, 포스터, 트레일러 등과 일대다 관계

- ### Theater

  <img width="895" height="571" alt="Image" src="https://github.com/user-attachments/assets/30426e7a-6bd7-48ea-a5fa-3b52799253be" />

  Theater(영화관) 1:N Screen(상영관) 1:N Showtime(상영 시간표)

  Screen(상영관) 1:N Seat(좌석) 1:N ReservedSeat(예매 좌석)


- ### Reservation

  <img width="503" height="302" alt="Image" src="https://github.com/user-attachments/assets/4e7c2334-620e-46f1-a876-c8858fe7d88b" />

  Showtime(상영 시간표) 1:N Ticket(예매)

  Ticket(예매) 1:N ReservedSeat(예매 좌석)

  Showtime(상영 시간표) 1:N ReservedSeat(예매 좌석)

- ### 매점

  <img width="520" height="291" alt="Image" src="https://github.com/user-attachments/assets/059647de-fe6a-456c-a9d2-b4c6d5fefa1d" />

  Theater(영화관) 1:N Order(주문) 1:N OrderItem(주문 상품) 1:N Product(상품)

  Product(상품)-Theater(영화관) 1:N Stock(재고)


- ### 명세

    | 기능                   | Method | Endpoint | Request                                                                                      | Response (예시) |
    |----------------------|--------|----------|----------------------------------------------------------------------------------------------|-----------------|
    | 영화 목록 조회             | `GET` | `/movies` | -                                                                                            | ```[ { "movieId": 1, "title": "범죄도시 5", "ageRating": "AGE_15", "bookingRate": 35.5, "releaseDate": "2025-08-01T00:00:00", "totalAudience": 1200000, "eggNum": 92.1, "posterUrl": "https://example.com/poster1.jpg" } ]``` |
    | 특정 영화 상세 조회          | `GET` | `/movies/{movieId}` | `movieId`                                                                                    | ```{ "movieId": 1, "title": "범죄도시 5", "ageRating": "AGE_15", "releaseDate": "2025-08-01T00:00:00", "runtime": 125, "genre": "Action", "bookingRate": 35.5, "totalAudience": 1200000, "eggNum": 92.1, "prologue": "마석도 형사의 귀환!", "persons": [], "posters": [ { "posterId": 1, "imageUrl": "https://example.com/poster1.jpg" } ], "events": [ { "eventId": 1, "title": "범죄도시 5 GV 이벤트", "description": "주연 배우 무대인사" } ], "reviews": [ { "reviewId": 1, "content": "재밌다!", "rating": 5, "username": "sangeun" } ], "trailers": [ { "reviewId": 1, "videoUrl": "https://youtube.com/trailer1", "description": "메인 예고편" } ] }``` |
    | 특정 영화가 상영하는 영화관 조회   | `GET` | `/movies/{movieId}/theaters` | `movieId`                                                                                    | ```[ { "id": 1, "name": "용산CGV", "address": "서울시 용산구 한강대로", "accessInfo": "1호선 용산역 연결", "parkingInfo": "지하 3층 주차장", "region": "서울" } ]``` |
    | 특정 영화-영화관의 상영 시간표 조회 | `GET` | `/movies/{movieId}/theaters/{theaterId}/showtimes` | `movieId`, `theaterId`                                                                       | ```[ { "showtimeId": 1, "startTime": "2025-09-22T10:00:00", "endTime": "2025-09-22T12:00:00", "screenName": "1관", "screenType": "IMAX", "totalSeats": 200 } ]``` |
    | 극장 목록 조회             | `GET` | `/theaters` | -                                                                                            | ```[ { "id": 1, "name": "용산CGV", "address": "서울시 용산구 한강대로", "accessInfo": "1호선 용산역 연결", "parkingInfo": "지하 3층 주차장", "region": "서울" } ]``` |
    | 극장 상세 조회             | `GET` | `/theaters/{id}` | `theaterId`                                                                                  | ```{ "id": 1, "name": "용산CGV", "address": "서울시 용산구 한강대로", "accessInfo": "1호선 용산역 연결", "parkingInfo": "지하 3층 주차장", "region": "서울" }``` |
    | 특정 상영 시간표 좌석 조회      | `GET` | `/showtimes/{showtimeId}/seats` | `showtimeId`                                                                                 | ```{ "showtimeId": 1, "screenName": "1관", "totalSeats": 200, "seats": [ { "seatId": 1, "row": "A", "col": "1", "status": "AVAILABLE" }, { "seatId": 2, "row": "A", "col": "2", "status": "AVAILABLE" } ] }``` |
    | 티켓 예매                | `POST` | `/tickets/reserve` | ```{ "showtimeId": 1, "userId": 1, "generalCount": 1, "youthCount": 0, "seatIds": ["3"] }``` | ```{ "ticketId": 5, "showtimeId": 1, "generalCount": 1, "youthCount": 0, "finalPrice": 12000, "reservedSeats": [ "A2" ] }``` |
    | 티켓 취소                | `DELETE` | `/tickets/cancel/{ticketId}` | `ticketId`                                                                                   | `"예매가 취소되었습니다."` |

- ### 예매 과정
  1. 영화 선택
     
     보고싶은 영화 선택(`/movies` or `/movies/{movieID}`)
  2. 영화관 선택
     
     선택한 영화를 상영하는 극장 조회 및 선택(`/movies/{movieId}/theaters`)
  3. 상영 시간표 조회(`/movies/{movieId}/theaters/{theaterId}/showtimes`)
  4. 좌석 조회
  
     예매 가능한 좌석 조회(`/showtimes/{showtimeId}/seats`)
  5. 좌석 예매

     청소년/성인 인원 수, 좌석 선택 후 예매(`/tickets/reserve`)

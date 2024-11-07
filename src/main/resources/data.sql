-- users 테이블 초기 데이터
INSERT INTO users (nickname, user_image_url, name, email, birthdate, phone, address, created_date, modified_date)
VALUES
    ('johndoe1', 'https://example.com/johndoe1.jpg', 'John Doe 1', 'john.doe1@example.com', '1990-01-01', '123-456-7890', '123 Main St, City, Country', NOW(), NOW()),
    ('johndoe2', 'https://example.com/johndoe2.jpg', 'John Doe 2', 'john.doe2@example.com', '1991-01-01', '123-456-7891', '124 Main St, City, Country', NOW(), NOW()),
    ('johndoe3', 'https://example.com/johndoe3.jpg', 'John Doe 3', 'john.doe3@example.com', '1992-01-01', '123-456-7892', '125 Main St, City, Country', NOW(), NOW()),
    ('johndoe4', 'https://example.com/johndoe4.jpg', 'John Doe 4', 'john.doe4@example.com', '1993-01-01', '123-456-7893', '126 Main St, City, Country', NOW(), NOW()),
    ('johndoe5', 'https://example.com/johndoe5.jpg', 'John Doe 5', 'john.doe5@example.com', '1994-01-01', '123-456-7894', '127 Main St, City, Country', NOW(), NOW()),
    ('janedoe1', 'https://example.com/janedoe1.jpg', 'Jane Doe 1', 'jane.doe1@example.com', '1990-02-01', '987-654-3210', '223 Main St, City, Country', NOW(), NOW()),
    ('janedoe2', 'https://example.com/janedoe2.jpg', 'Jane Doe 2', 'jane.doe2@example.com', '1991-02-01', '987-654-3211', '224 Main St, City, Country', NOW(), NOW()),
    ('janedoe3', 'https://example.com/janedoe3.jpg', 'Jane Doe 3', 'jane.doe3@example.com', '1992-02-01', '987-654-3212', '225 Main St, City, Country', NOW(), NOW()),
    ('janedoe4', 'https://example.com/janedoe4.jpg', 'Jane Doe 4', 'jane.doe4@example.com', '1993-02-01', '987-654-3213', '226 Main St, City, Country', NOW(), NOW()),
    ('janedoe5', 'https://example.com/janedoe5.jpg', 'Jane Doe 5', 'jane.doe5@example.com', '1994-02-01', '987-654-3214', '227 Main St, City, Country', NOW(), NOW());

-- user_hashtags 테이블 초기 데이터
INSERT INTO user_hashtags (user_id, hash_tags)
VALUES
    (1, 'SERENITY'),
    (1, 'JOYFUL'),
    (2, 'MELANCHOLY'),
    (2, 'NOSTALGIA'),
    (3, 'VIBRANCE'),
    (3, 'WONDER'),
    (4, 'DREAMLIKE'),
    (5, 'CONTEMPLATION'),
    (6, 'JOYFUL'),
    (7, 'LONELINESS'),
    (8, 'MYSTERY'),
    (9, 'SERENITY'),
    (10, 'NOSTALGIA');

-- artist_info 테이블 초기 데이터
INSERT INTO artist_info (user_id, nickname, artist_image_url, artist_type, total_followers, total_likes, about)
VALUES
    (1, 'ArtistJohn1', 'https://example.com/artistjohn1.jpg', 'BUSINESS', 100, 200, 'About Artist John 1'),
    (2, 'ArtistJohn2', 'https://example.com/artistjohn2.jpg', 'STUDENT', 150, 300, 'About Artist John 2'),
    (3, 'ArtistJane1', 'https://example.com/artistjane1.jpg', 'BUSINESS', 120, 220, 'About Artist Jane 1'),
    (4, 'ArtistJane2', 'https://example.com/artistjane2.jpg', 'STUDENT', 130, 250, 'About Artist Jane 2');

-- business_artist 테이블 초기 데이터
INSERT INTO business_artist (artist_info_id, business_number, open_date, head_name)
VALUES
    (1, 'B123456789', '2000-01-01', 'John Head 1'),
    (3, 'B987654321', '2005-02-01', 'Jane Head 1');

-- student_artist 테이블 초기 데이터
INSERT INTO student_artist (artist_info_id, school_email, school_name, major)
VALUES
    (2, 'john2@university.com', 'University A', 'Fine Arts'),
    (4, 'jane2@university.com', 'University B', 'Visual Arts');

-- social 테이블 초기 데이터
INSERT INTO social (follower_id, following_id)
VALUES
    (1, 2),
    (1, 3),
    (2, 4),
    (3, 1),
    (4, 2);

-- product 테이블 초기 데이터 삽입
INSERT INTO product (name, category, size, price, description, preferred_location, thumbnail_url, artist_info_id, created_date, modified_date)
VALUES
    ('Mountain Landscape', 'PICTURE', '100x150', 1500000, 'Beautiful mountain landscape painting.', 'Seoul, South Korea', 'https://example.com/thumbnail1.jpg', 1, NOW(), NOW()),
    ('Modern Sculpture', 'PIECE', '50x80', 2500000, 'Modern art sculpture made of steel.', 'New York, USA', 'https://example.com/thumbnail2.jpg', 3, NOW(), NOW()),
    ('Oriental Artwork', 'ORIENTAL', '120x180', 2000000, 'Traditional oriental painting.', 'Beijing, China', 'https://example.com/thumbnail3.jpg', 2, NOW(), NOW());

-- product_hashtags 테이블 초기 데이터 삽입
INSERT INTO product_hashtags (product_id, hash_tags)
VALUES
    (1, 'SERENITY'),
    (1, 'VIBRANCE'),
    (2, 'CONTEMPLATION'),
    (3, 'MYSTERY'),
    (3, 'DREAMLIKE');

-- product_image 테이블 초기 데이터 삽입
INSERT INTO product_image (photo_url, product_id, uuid)
VALUES
    ('https://example.com/product1_image1.jpg', 1, 'uuid-1234'),
    ('https://example.com/product1_image2.jpg', 1, 'uuid-5678'),
    ('https://example.com/product2_image1.jpg', 2, 'uuid-91011'),
    ('https://example.com/product3_image1.jpg', 3, 'uuid-121314');

-- likes 테이블 초기 데이터 삽입
INSERT INTO likes (user_id, product_id)
VALUES
    (1, 1),
    (2, 1),
    (3, 2),
    (4, 3),
    (5, 3);

-- orders 테이블 초기 데이터 삽입
INSERT INTO orders (user_id, product_id, status)
VALUES
    (1, 1, 'ORDER'),
    (2, 2, 'ORDER'),
    (3, 3, 'CANCEL'),
    (4, 1, 'ORDER'),
    (5, 2, 'CANCEL');


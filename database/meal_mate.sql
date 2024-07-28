CREATE DATABASE IF NOT EXISTS `meal_mate`;

USE `meal_mate`;

DROP TABLE IF EXISTS `address`;

CREATE TABLE `address` (
    `id` bigint UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id` bigint UNSIGNED NOT NULL COMMENT 'user id',
    `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'customer name',
    `phone` varchar(11) COLLATE utf8_bin NOT NULL COMMENT 'phone number',
    `province_name` varchar(32) DEFAULT NULL,
    `city_name` varchar(32) DEFAULT NULL,
    `district_name` varchar(32) DEFAULT NULL,
    `detail` varchar(200) DEFAULT NULL COMMENT 'detailed address',
    `label` varchar(100) DEFAULT NULL COMMENT 'label for address, e.g. home, work',
    `is_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0(default): no, 1: yes'
) ENGINE = InnoDB AUTO_INCREMENT = 2;

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
    `id` bigint UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `type` tinyint DEFAULT NULL COMMENT '1: dish category, 2: meal category',
    `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT 'category name',
    `ranking` int NOT NULL DEFAULT '0',
    `status` int DEFAULT NULL COMMENT '0: disabled, 1: enabled',
    `create_time` datetime DEFAULT NULL,
    `update_time` datetime DEFAULT NOW(),
    `create_user` bigint UNSIGNED DEFAULT NULL COMMENT 'user id who created this record',
    `update_user` bigint UNSIGNED DEFAULT NULL COMMENT 'user id who updated this record',
    UNIQUE `idx_category_name` (`name`)
) ENGINE = InnoDB AUTO_INCREMENT = 23 COMMENT = 'dish/meal category';

INSERT INTO
    `category`
VALUES
    (
        11,
        1,
        'drink',
        10,
        1,
        '2023-06-09 22:09:18',
        '2023-06-09 22:09:18',
        1,
        1
    );

INSERT INTO
    `category`
VALUES
    (
        12,
        1,
        'stir fry',
        9,
        1,
        '2023-06-09 22:09:32',
        '2023-06-09 22:18:53',
        1,
        1
    );

INSERT INTO
    `category`
VALUES
    (
        13,
        2,
        'popular meal',
        12,
        1,
        '2023-06-09 22:11:38',
        '2023-06-10 11:04:40',
        1,
        1
    );

INSERT INTO
    `category`
VALUES
    (
        15,
        2,
        'business meal',
        13,
        1,
        '2023-06-09 22:14:10',
        '2023-06-10 11:04:48',
        1,
        1
    );

INSERT INTO
    `category`
VALUES
    (
        16,
        1,
        'pizza',
        4,
        1,
        '2023-06-09 22:15:37',
        '2023-08-31 14:27:25',
        1,
        1
    );

INSERT INTO
    `category`
VALUES
    (
        17,
        1,
        'burger',
        5,
        1,
        '2023-06-09 22:16:14',
        '2023-08-31 14:39:44',
        1,
        1
    );

INSERT INTO
    `category`
VALUES
    (
        18,
        1,
        'dessert',
        6,
        1,
        '2023-06-09 22:17:42',
        '2023-06-09 22:17:42',
        1,
        1
    );

INSERT INTO
    `category`
VALUES
    (
        19,
        1,
        'salad',
        7,
        1,
        '2023-06-09 22:18:12',
        '2023-06-09 22:18:28',
        1,
        1
    );

INSERT INTO
    `category`
VALUES
    (
        20,
        1,
        'snack',
        8,
        1,
        '2023-06-09 22:22:29',
        '2023-06-09 22:23:45',
        1,
        1
    );

INSERT INTO
    `category`
VALUES
    (
        21,
        1,
        'soup',
        11,
        1,
        '2023-06-10 10:51:47',
        '2023-06-10 10:51:47',
        1,
        1
    );

DROP TABLE IF EXISTS `dish`;

CREATE TABLE `dish` (
    `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'dish name',
    `category_id` bigint NOT NULL,
    `price` decimal(10, 2) DEFAULT NULL,
    `image` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `status` tinyint(1) DEFAULT '1' COMMENT '0:sold out, 1: available',
    `create_time` datetime DEFAULT NULL,
    `update_time` datetime DEFAULT NOW(),
    `create_user` bigint UNSIGNED DEFAULT NULL,
    `update_user` bigint UNSIGNED DEFAULT NULL,
    UNIQUE `idx_dish_name` (`name`)
) ENGINE = InnoDB AUTO_INCREMENT = 70;

INSERT INTO
    `dish`
VALUES
    (
        46,
        'Blueberry Banana Smoothie',
        11,
        6.00,
        'https://cdn.dummyjson.com/recipe-images/25.webp',
        '',
        1,
        '2023-06-09 22:40:47',
        '2023-06-09 22:40:47',
        1,
        1
    );

INSERT INTO
    `dish`
VALUES
    (
        49,
        'Classic Mojito',
        11,
        2.00,
        'https://cdn.dummyjson.com/recipe-images/40.webp',
        '',
        1,
        '2023-06-10 09:30:17',
        '2023-06-10 09:30:17',
        1,
        1
    );

INSERT INTO
    `dish`
VALUES
    (
        47,
        'Japanese Matcha Green Tea Ice Cream',
        18,
        4.00,
        'https://cdn.dummyjson.com/recipe-images/34.webp',
        'yummy',
        1,
        '2023-06-10 09:18:49',
        '2023-06-10 09:18:49',
        1,
        1
    );

INSERT INTO
    `dish`
VALUES
    (
        48,
        'Brazilian Chocolate Brigadeiros',
        18,
        4.00,
        'https://cdn.dummyjson.com/recipe-images/35.webp',
        '',
        1,
        '2023-06-10 09:22:54',
        '2023-06-10 09:22:54',
        1,
        1
    );

INSERT INTO
    `dish`
VALUES
    (
        50,
        'Teriyaki Chicken Stir-Fry',
        12,
        1.00,
        'https://cdn.dummyjson.com/recipe-images/42.webp',
        '',
        1,
        '2023-06-10 09:34:28',
        '2023-06-10 09:34:28',
        1,
        1
    );

INSERT INTO
    `dish`
VALUES
    (
        51,
        'Shrimp and Asparagus Stir-Fry',
        12,
        56.00,
        'https://cdn.dummyjson.com/recipe-images/44.webp',
        '',
        1,
        '2023-06-10 09:40:51',
        '2023-06-10 09:40:51',
        1,
        1
    );

INSERT INTO
    `dish`
VALUES
    (
        52,
        'Vegetarian Stir-Fry',
        12,
        66.00,
        'https://cdn.dummyjson.com/recipe-images/2.webp',
        '',
        1,
        '2023-06-10 09:46:02',
        '2023-06-10 09:46:02',
        1,
        1
    );

INSERT INTO
    `dish`
VALUES
    (
        53,
        'Beef and Broccoli Stir-Fry',
        20,
        38.00,
        'https://cdn.dummyjson.com/recipe-images/8.webp',
        'Beef, broccoli, soy sauce, garlic, ginger, brown sugar, cornstarch, sesame oil, red pepper flakes, green onions, sesame seeds',
        1,
        '2023-06-10 09:48:37',
        '2023-06-10 09:48:37',
        1,
        1
    );

INSERT INTO
    `dish`
VALUES
    (
        54,
        'Quinoa Salad with Avocado',
        19,
        18.00,
        'https://cdn.dummyjson.com/recipe-images/6.webp',
        'Baguette, tomatoes, fresh basil',
        1,
        '2023-06-10 09:51:46',
        '2023-06-10 09:51:46',
        1,
        1
    );

INSERT INTO
    `dish`
VALUES
    (
        55,
        'Caprese Salad',
        19,
        18.00,
        'https://cdn.dummyjson.com/recipe-images/9.webp',
        'Tomatoes, fresh mozzarella cheese, fresh basil leaves',
        1,
        '2023-06-10 09:53:37',
        '2023-06-10 09:53:37',
        1,
        1
    );

INSERT INTO
    `dish`
VALUES
    (
        56,
        'Moroccan Couscous Salad',
        19,
        18.00,
        'https://cdn.dummyjson.com/recipe-images/39.webp',
        'Couscous, chickpeas, cherry tomatoes, cucumber, red onion',
        1,
        '2023-06-10 09:55:44',
        '2023-06-10 09:55:44',
        1,
        1
    );

INSERT INTO
    `dish`
VALUES
    (
        57,
        'Mediterranean Quinoa Salad',
        19,
        18.00,
        'https://cdn.dummyjson.com/recipe-images/33.webp',
        'Quinoa, cucumber, cherry tomatoes, kalamata olives, red onion, feta cheese, fresh parsley, lemon juice, olive oil, salt, pepper',
        1,
        '2023-06-10 09:58:35',
        '2023-06-10 09:58:35',
        1,
        1
    );

INSERT INTO
    `dish`
VALUES
    (
        62,
        'Chocolate Chip Cookies',
        18,
        88.00,
        'https://cdn.dummyjson.com/recipe-images/3.webp',
        'Butter, sugar, brown sugar, eggs, vanilla extract, flour, baking soda, salt, chocolate chips',
        1,
        '2023-06-10 10:33:05',
        '2023-06-10 10:33:05',
        1,
        1
    );

INSERT INTO
    `dish`
VALUES
    (
        65,
        'Classic Margherita Pizza',
        16,
        68.00,
        'https://cdn.dummyjson.com/recipe-images/1.webp',
        'Pizza dough, tomato sauce, fresh mozzarella cheese, fresh basil leaves',
        1,
        '2023-06-10 10:41:08',
        '2023-06-10 10:41:08',
        1,
        1
    );

INSERT INTO
    `dish`
VALUES
    (
        66,
        'Italian Margherita Pizza',
        16,
        119.00,
        'https://cdn.dummyjson.com/recipe-images/45.webp',
        'Pizza dough, tomato sauce, fresh mozzarella cheese, fresh basil leaves',
        1,
        '2023-06-10 10:42:42',
        '2023-06-10 10:42:42',
        1,
        1
    );

INSERT INTO
    `dish`
VALUES
    (
        68,
        'Japanese Ramen Soup',
        21,
        4.00,
        'https://cdn.dummyjson.com/recipe-images/16.webp',
        'Ramen noodles, chicken broth, soy sauce, mirin, green onions, nori, eggs, pork belly, garlic, ginger, sesame oil, sesame seeds, chili oil, shiitake mushrooms, bamboo shoots, bean sprouts, corn, fish cakes, seaweed, pickled ginger, black garlic',
        1,
        '2023-06-10 10:54:25',
        '2023-06-10 10:54:25',
        1,
        1
    );

INSERT INTO
    `dish`
VALUES
    (
        69,
        'Russian Borscht',
        21,
        6.00,
        'https://cdn.dummyjson.com/recipe-images/27.webp',
        'Beetroot, cabbage, potatoes, carrots, onions, garlic, dill',
        1,
        '2023-06-10 10:55:02',
        '2023-06-10 10:55:02',
        1,
        1
    );

DROP TABLE IF EXISTS `flavor`;

CREATE TABLE `flavor` (
    `id` bigint UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `dish_id` bigint UNSIGNED NOT NULL,
    `name` varchar(32) COLLATE utf8_bin DEFAULT NULL,
    `value` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'list of levels of food flavor'
) ENGINE = InnoDB AUTO_INCREMENT = 104;

INSERT INTO
  `flavor`
VALUES
  (
    40,
    10,
    'Sweetness',
    '[\"No Sugar\",\"Less Sugar\",\"Half Sugar\",\"More Sugar\",\"Full Sugar\"]'
  );

INSERT INTO
  `flavor`
VALUES
  (41, 7, 'Avoidance', '[\"No Onion\",\"No Garlic\",\"No Cilantro\",\"No Spicy\"]');

INSERT INTO
  `flavor`
VALUES
  (42, 7, 'Temperature', '[\"Hot\",\"Room Temperature\",\"No Ice\",\"Less Ice\",\"More Ice\"]');

INSERT INTO
  `flavor`
VALUES
  (45, 6, 'Avoidance', '[\"No Onion\",\"No Garlic\",\"No Cilantro\",\"No Spicy\"]');

INSERT INTO
  `flavor`
VALUES
  (46, 6, 'Spiciness', '[\"No Spicy\",\"Mild\",\"Medium\",\"Spicy\"]');

INSERT INTO
  `flavor`
VALUES
  (47, 5, 'Spiciness', '[\"No Spicy\",\"Mild\",\"Medium\",\"Spicy\"]');

INSERT INTO
  `flavor`
VALUES
  (48, 5, 'Sweetness', '[\"No Sugar\",\"Less Sugar\",\"Half Sugar\",\"More Sugar\",\"Full Sugar\"]');

INSERT INTO
  `flavor`
VALUES
  (49, 2, 'Sweetness', '[\"No Sugar\",\"Less Sugar\",\"Half Sugar\",\"More Sugar\",\"Full Sugar\"]');

INSERT INTO
  `flavor`
VALUES
  (50, 4, 'Sweetness', '[\"No Sugar\",\"Less Sugar\",\"Half Sugar\",\"More Sugar\",\"Full Sugar\"]');

INSERT INTO
  `flavor`
VALUES
  (51, 3, 'Sweetness', '[\"No Sugar\",\"Less Sugar\",\"Half Sugar\",\"More Sugar\",\"Full Sugar\"]');

INSERT INTO
  `flavor`
VALUES
  (52, 3, 'Avoidance', '[\"No Onion\",\"No Garlic\",\"No Cilantro\",\"No Spicy\"]');

INSERT INTO
  `flavor`
VALUES
  (86, 52, 'Avoidance', '[\"No Onion\",\"No Garlic\",\"No Cilantro\",\"No Spicy\"]');

INSERT INTO
  `flavor`
VALUES
  (87, 52, 'Spiciness', '[\"No Spicy\",\"Mild\",\"Medium\",\"Spicy\"]');

INSERT INTO
  `flavor`
VALUES
  (88, 51, 'Avoidance', '[\"No Onion\",\"No Garlic\",\"No Cilantro\",\"No Spicy\"]');

INSERT INTO
  `flavor`
VALUES
  (89, 51, 'Spiciness', '[\"No Spicy\",\"Mild\",\"Medium\",\"Spicy\"]');

INSERT INTO
  `flavor`
VALUES
  (92, 53, 'Avoidance', '[\"No Onion\",\"No Garlic\",\"No Cilantro\",\"No Spicy\"]');

INSERT INTO
  `flavor`
VALUES
  (93, 53, 'Spiciness', '[\"No Spicy\",\"Mild\",\"Medium\",\"Spicy\"]');

INSERT INTO
  `flavor`
VALUES
  (94, 54, 'Avoidance', '[\"No Onion\",\"No Garlic\",\"No Cilantro\"]');

INSERT INTO
  `flavor`
VALUES
  (95, 56, 'Avoidance', '[\"No Onion\",\"No Garlic\",\"No Cilantro\",\"No Spicy\"]');

INSERT INTO
  `flavor`
VALUES
  (96, 57, 'Avoidance', '[\"No Onion\",\"No Garlic\",\"No Cilantro\",\"No Spicy\"]');

INSERT INTO
  `flavor`
VALUES
  (97, 60, 'Avoidance', '[\"No Onion\",\"No Garlic\",\"No Cilantro\",\"No Spicy\"]');

INSERT INTO
  `flavor`
VALUES
  (101, 66, 'Spiciness', '[\"No Spicy\",\"Mild\",\"Medium\",\"Spicy\"]');

INSERT INTO
  `flavor`
VALUES
  (102, 67, 'Spiciness', '[\"No Spicy\",\"Mild\",\"Medium\",\"Spicy\"]');

INSERT INTO
  `flavor`
VALUES
  (103, 65, 'Spiciness', '[\"No Spicy\",\"Mild\",\"Medium\",\"Spicy\"]');

DROP TABLE IF EXISTS `employee`;

CREATE TABLE `employee` (
    `id` bigint UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT 'customer name',
    `user_name` varchar(32) COLLATE utf8_bin NOT NULL,
    `password` varchar(64) COLLATE utf8_bin NOT NULL,
    `phone` varchar(11) COLLATE utf8_bin NOT NULL COMMENT 'phone number',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '0: disabled, 1(default): enabled',
    `create_time` datetime DEFAULT NULL,
    `update_time` datetime DEFAULT NOW(),
    `create_user` bigint UNSIGNED DEFAULT NULL,
    `update_user` bigint UNSIGNED DEFAULT NULL,
    `shop_id` bigint UNSIGNED DEFAULT NULL,
    UNIQUE `idx_username` (`user_name`)
) ENGINE = InnoDB AUTO_INCREMENT = 2;

INSERT INTO
    `employee`
VALUES
    (
        1,
        'admin',
        'admin',
        'e10adc3949ba59abbe56e057f20f883e',
        '13812312312',
        '1',
        '2023-02-15 15:51:20',
        '2023-02-17 09:16:20',
        10,
        1,
        1
    );

DROP TABLE IF EXISTS `order_detail`;

CREATE TABLE `order_detail` (
    `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(32) COLLATE utf8_bin DEFAULT NULL,
    `image` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `order_id` bigint UNSIGNED NOT NULL,
    `dish_id` bigint UNSIGNED DEFAULT NULL,
    `meal_id` bigint UNSIGNED DEFAULT NULL,
    `flavor` varchar(50) COLLATE utf8_bin DEFAULT NULL,
    `quantity` int UNSIGNED NOT NULL DEFAULT '1',
    `price` decimal(10, 2) NOT NULL
) ENGINE = InnoDB AUTO_INCREMENT = 5;

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
    `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `serial_number` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'serial number',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '1: pending payment 2: pending order 3: order received 4: delivery in progress 5: completed 6: canceled 7: refunded',
    `user_id` bigint UNSIGNED NOT NULL,
    `address_id` bigint UNSIGNED NOT NULL,
    `order_time` datetime NOT NULL,
    `checkout_time` datetime DEFAULT NULL,
    `pay_method` int NOT NULL DEFAULT '1' COMMENT '1: alipay 2: wechat_pay 3: card 4: paypal 5: cashapp',
    `pay_status` tinyint NOT NULL DEFAULT '0' COMMENT 'payment status 0: unpaid 1: paid 2: refunded',
    `price` decimal(10, 2) NOT NULL,
    `note` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT 'added messenge',
    `phone` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT 'phone number',
    `address` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `user_name` varchar(32) COLLATE utf8_bin DEFAULT NULL,
    `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'customer name',
    `cancel_reason` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `rejection_reason` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `cancel_time` datetime DEFAULT NULL,
    `estimated_delivery_time` datetime DEFAULT NULL,
    `delivery_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0: choose specific time, 1(default): immediate delivery',
    `delivery_time` datetime DEFAULT NULL,
    `packaging_fee` int DEFAULT NULL,
    `tableware_number` int UNSIGNED DEFAULT NULL,
    `tableware_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0: choose specific quantity 1(default): provide according to meal quantity',
    `payment_intent_id` varchar(50) DEFAULT NULL
  ) ENGINE = InnoDB AUTO_INCREMENT = 4;

DROP TABLE IF EXISTS `meal`;

CREATE TABLE `meal` (
  `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `category_id` bigint NOT NULL,
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  `price` decimal(10, 2) NOT NULL,
  `status` int DEFAULT '1' COMMENT 'selling status 0: sold out 1: available',
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `image` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` bigint DEFAULT NULL,
  `update_user` bigint DEFAULT NULL,
  UNIQUE KEY `idx_meal_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=32;

DROP TABLE IF EXISTS `meal_dish`;

CREATE TABLE `meal_dish` (
    `id` bigint UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `meal_id` bigint UNSIGNED DEFAULT NULL,
    `dish_id` bigint UNSIGNED DEFAULT NULL,
    `name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
    `price` decimal(10, 2) DEFAULT NULL,
    `quantity` int DEFAULT NULL COMMENT 'number of dishes in the meal'
) ENGINE = InnoDB AUTO_INCREMENT = 47;

DROP TABLE IF EXISTS `cart`;

CREATE TABLE `cart` (
    `id` bigint UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
    `image` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `user_id` bigint UNSIGNED NOT NULL,
    `dish_id` bigint UNSIGNED DEFAULT NULL,
    `meal_id` bigint UNSIGNED DEFAULT NULL,
    `flavor` varchar(50) COLLATE utf8_bin DEFAULT NULL,
    `quantity` int UNSIGNED NOT NULL DEFAULT '1',
    `price` decimal(10, 2) NOT NULL,
    `create_time` datetime DEFAULT NULL
) ENGINE = InnoDB AUTO_INCREMENT = 9;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `id` bigint UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `openid` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT 'wechat user id',
    `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'customer name',
    `phone` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT 'phone number',
    `profile_image` varchar(500) COLLATE utf8_bin DEFAULT NULL,
    `create_time` datetime DEFAULT NULL
) ENGINE = InnoDB AUTO_INCREMENT = 4;
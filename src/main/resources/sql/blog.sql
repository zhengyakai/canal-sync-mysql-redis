/*
 Navicat Premium Data Transfer

 Source Server         : tx-yakai
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : 58.87.67.162:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 18/12/2018 17:04:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `author` varchar(20) NOT NULL DEFAULT '',
  `title` varchar(100) NOT NULL,
  `subtitle` varchar(100) NOT NULL,
  `content` longtext NOT NULL,
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

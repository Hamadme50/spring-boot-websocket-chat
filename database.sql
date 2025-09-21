-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Sep 21, 2025 at 10:50 PM
-- Server version: 10.11.6-MariaDB-0+deb12u1
-- PHP Version: 8.1.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `chatlinksdb`
--
CREATE DATABASE IF NOT EXISTS `chatlinksdb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `chatlinksdb`;

-- --------------------------------------------------------

--
-- Table structure for table `chatroom`
--

CREATE TABLE `chatroom` (
  `chatroom_id` bigint(20) NOT NULL,
  `chatroom_Name` varchar(50) NOT NULL,
  `chatroom_domain` varchar(100) NOT NULL,
  `chatroom_theme` varchar(10) NOT NULL,
  `chatroom_paid` varchar(1) NOT NULL,
  `chatroom_date` datetime NOT NULL,
  `chatroom_status` varchar(1) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `chatroom_topic` varchar(200) DEFAULT NULL,
  `chatroom_antispam` varchar(1) NOT NULL,
  `radio` varchar(100) DEFAULT NULL,
  `guest` varchar(1) NOT NULL DEFAULT 'Y',
  `register` varchar(1) NOT NULL DEFAULT 'Y',
  `design` varchar(3) NOT NULL DEFAULT 'M'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `customer_id` bigint(20) NOT NULL,
  `customer_name` varchar(30) NOT NULL,
  `customer_email` varchar(80) NOT NULL,
  `customer_password` varchar(100) NOT NULL,
  `customer_date` date NOT NULL,
  `owner` varchar(1) NOT NULL,
  `paid` varchar(1) DEFAULT 'Y',
  `temp_password` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ipban`
--

CREATE TABLE `ipban` (
  `ban_id` bigint(20) NOT NULL,
  `ban_ip` varchar(45) NOT NULL,
  `username` varchar(16) DEFAULT NULL,
  `chatroom_id` bigint(20) NOT NULL,
  `ban_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mainchat`
--

CREATE TABLE `mainchat` (
  `mainchat_id` bigint(20) NOT NULL,
  `chat_id` varchar(50) NOT NULL,
  `chat_content` varchar(2000) NOT NULL,
  `chat_username` varchar(100) NOT NULL,
  `chat_userid` bigint(20) NOT NULL,
  `chatroom_id` bigint(20) NOT NULL,
  `chat_namecolor` varchar(50) DEFAULT NULL,
  `chat_dp` varchar(100) DEFAULT NULL,
  `chat_textcolor` varchar(50) DEFAULT NULL,
  `chat_bold` varchar(50) DEFAULT NULL,
  `chat_font` varchar(50) DEFAULT NULL,
  `chat_userstatus` varchar(10) NOT NULL,
  `chat_type` varchar(10) NOT NULL,
  `chat_msgdate` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `priavte_chat`
--

CREATE TABLE `priavte_chat` (
  `pm_id` bigint(20) NOT NULL,
  `from_user` bigint(20) NOT NULL,
  `to_user` bigint(20) NOT NULL,
  `pm_content` varchar(1000) NOT NULL,
  `pm_date` datetime NOT NULL,
  `pm_seen` varchar(1) NOT NULL,
  `chatroom_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `rank`
--

CREATE TABLE `rank` (
  `rank_id` bigint(20) NOT NULL,
  `rank_name` varchar(50) NOT NULL,
  `rank_code` varchar(10) NOT NULL,
  `rank_icon` varchar(200) NOT NULL,
  `rank_kick` varchar(1) NOT NULL,
  `rank_ban` varchar(1) NOT NULL,
  `rank_spam` varchar(1) NOT NULL,
  `rank_mute` varchar(1) NOT NULL,
  `rank_changenick` varchar(1) NOT NULL DEFAULT 'N',
  `rank_del_msg` varchar(1) NOT NULL,
  `chatroom_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` bigint(20) NOT NULL,
  `user_name` varchar(35) NOT NULL,
  `user_password` varchar(100) NOT NULL,
  `user_joindate` date NOT NULL,
  `user_status` varchar(1) NOT NULL,
  `user_activitydate` datetime NOT NULL,
  `user_birthdate` date DEFAULT NULL,
  `user_gender` varchar(1) NOT NULL,
  `user_dp` varchar(100) NOT NULL,
  `user_rj` varchar(1) NOT NULL,
  `user_ip` varchar(45) NOT NULL,
  `user_spam` varchar(1) NOT NULL,
  `user_ban` varchar(1) NOT NULL,
  `user_tweet` varchar(40) DEFAULT NULL,
  `user_about` varchar(150) DEFAULT NULL,
  `user_name_color` varchar(15) NOT NULL,
  `user_kick` varchar(1) DEFAULT NULL,
  `user_email` varchar(80) NOT NULL,
  `user_points` float(8,2) NOT NULL,
  `chatroom_id` bigint(20) NOT NULL,
  `user_rank` bigint(20) NOT NULL,
  `user_text_color` varchar(10) DEFAULT NULL,
  `user_bold` varchar(1) NOT NULL DEFAULT 'N',
  `user_font` varchar(10) DEFAULT NULL,
  `owner` varchar(1) NOT NULL DEFAULT 'N',
  `temp_password` varchar(100) DEFAULT NULL,
  `session` varchar(300) DEFAULT NULL,
  `file` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chatroom`
--
ALTER TABLE `chatroom`
  ADD PRIMARY KEY (`chatroom_id`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customer_id`),
  ADD UNIQUE KEY `customer_email` (`customer_email`);

--
-- Indexes for table `ipban`
--
ALTER TABLE `ipban`
  ADD PRIMARY KEY (`ban_id`);

--
-- Indexes for table `mainchat`
--
ALTER TABLE `mainchat`
  ADD PRIMARY KEY (`mainchat_id`);

--
-- Indexes for table `priavte_chat`
--
ALTER TABLE `priavte_chat`
  ADD PRIMARY KEY (`pm_id`);

--
-- Indexes for table `rank`
--
ALTER TABLE `rank`
  ADD PRIMARY KEY (`rank_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `UNIQUEUSER` (`user_name`,`chatroom_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `chatroom`
--
ALTER TABLE `chatroom`
  MODIFY `chatroom_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `customer_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ipban`
--
ALTER TABLE `ipban`
  MODIFY `ban_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `mainchat`
--
ALTER TABLE `mainchat`
  MODIFY `mainchat_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `rank`
--
ALTER TABLE `rank`
  MODIFY `rank_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
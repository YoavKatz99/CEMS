-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: cems
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `test_question`
--

DROP TABLE IF EXISTS `test_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_question` (
  `test_id` varchar(6) NOT NULL,
  `question_id` varchar(5) NOT NULL,
  `points` varchar(3) NOT NULL,
  PRIMARY KEY (`test_id`,`question_id`),
  KEY `question_id_idx` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_question`
--

LOCK TABLES `test_question` WRITE;
/*!40000 ALTER TABLE `test_question` DISABLE KEYS */;
INSERT INTO `test_question` VALUES ('010101','01124','10'),('010101','01126','10'),('010101','01127','30'),('010101','01128','30'),('010101','01129','10'),('010101','02123','10'),('010107','01124','40'),('010107','01126','20'),('010107','01128','20'),('010202','01131','30'),('010202','01132','40'),('010202','01133','30'),('010203','01137','30'),('010203','01138','30'),('010203','01139','40'),('020205','02135','20'),('020205','02149','20'),('020205','02150','20'),('020205','02151','20'),('020205','02152','20'),('020206','02149','50'),('020206','02151','40'),('020206','02152','10'),('020309','02153','20'),('020309','02154','20'),('020309','02155','10'),('020309','02156','30'),('020309','02157','20'),('020310','02154','10'),('020310','02155','20'),('020310','02156','30'),('020310','02157','40'),('030104','03130','20'),('030104','03140','20'),('030104','03141','20'),('030104','03142','20'),('030104','03143','20'),('030111','03130','30'),('030111','03141','30'),('030111','03143','40'),('030208','03144','20'),('030208','03145','20'),('030208','03146','20'),('030208','03147','20'),('030208','03148','20');
/*!40000 ALTER TABLE `test_question` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-20 15:59:05

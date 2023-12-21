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
-- Table structure for table `test_stats`
--

DROP TABLE IF EXISTS `test_stats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_stats` (
  `test_id` varchar(6) NOT NULL,
  `average` double NOT NULL,
  `median` int NOT NULL,
  `decile1` int NOT NULL,
  `decile2` int NOT NULL,
  `decile3` int NOT NULL,
  `decile4` int NOT NULL,
  `decile5` int NOT NULL,
  `decile6` int NOT NULL,
  `decile7` int NOT NULL,
  `decile8` int NOT NULL,
  `decile9` int NOT NULL,
  `decile10` int NOT NULL,
  `course` varchar(45) NOT NULL,
  `date` date DEFAULT NULL,
  `time_allocated` varchar(45) NOT NULL,
  `students_started` int NOT NULL,
  `submitted` int NOT NULL,
  `author` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_stats`
--

LOCK TABLES `test_stats` WRITE;
/*!40000 ALTER TABLE `test_stats` DISABLE KEYS */;
INSERT INTO `test_stats` VALUES ('010101',40,90,0,1,0,3,2,1,0,0,0,1,'Algebra','2023-06-20','100',8,7,'yael kanter'),('010107',56.67,80,0,0,0,0,3,0,1,0,2,0,'Algebra','2023-06-20','90',6,5,'yael kanter'),('010202',68.33,100,0,0,0,1,1,0,0,2,0,2,'Calculus','2023-06-20','120',6,5,'iris kanter'),('010203',0,0,0,0,0,0,0,0,0,0,0,0,'Calculus','2023-06-20','0',0,0,'iris kanter'),('020205',60,80,0,0,1,0,2,0,1,0,2,1,'Java','2023-06-20','60',7,6,'galit aharon'),('020206',0,0,0,0,0,0,0,0,0,0,0,0,'Java','2023-06-20','0',0,0,'galit aharon'),('020309',0,0,0,0,0,0,0,0,0,0,0,0,'0','2023-06-20','0',0,0,' '),('020310',0,0,0,0,0,0,0,0,0,0,0,0,'0','2023-06-20','0',0,0,' '),('030104',0,0,0,0,0,0,0,0,0,0,0,0,'Mechanics','2023-06-20','0',0,0,'alon aloni'),('030111',0,0,0,0,0,0,0,0,0,0,0,0,'0','2023-06-20','0',0,0,' '),('030208',0,0,0,0,0,0,0,0,0,0,0,0,'0','2023-06-20','0',0,0,' ');
/*!40000 ALTER TABLE `test_stats` ENABLE KEYS */;
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

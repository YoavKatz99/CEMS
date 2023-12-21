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
-- Table structure for table `tests`
--

DROP TABLE IF EXISTS `tests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tests` (
  `Select` int DEFAULT NULL,
  `ID` varchar(6) NOT NULL,
  `Subject` varchar(45) NOT NULL,
  `Course` varchar(45) NOT NULL,
  `Author` varchar(45) NOT NULL,
  `Status` varchar(45) NOT NULL,
  `Duration` int NOT NULL,
  `TestCode` varchar(5) NOT NULL,
  `LecturerText` varchar(45) DEFAULT NULL,
  `StudentText` varchar(45) DEFAULT NULL,
  `NumTest` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tests`
--

LOCK TABLES `tests` WRITE;
/*!40000 ALTER TABLE `tests` DISABLE KEYS */;
INSERT INTO `tests` VALUES (1,'010101','Math','Algebra','yael kanter','Done',100,'1421','Hello','Good Luck','01'),(NULL,'010107','Math','Algebra','yael kanter','Done',90,'1311','','','07'),(NULL,'010202','Math','Calculus','iris kanter','Done',120,'1312','pop quiz','good luck!','02'),(NULL,'010203','Math','Calculus','iris kanter','Inactive',90,'1759','mid quiz','Good Luck! take your time','03'),(NULL,'020205','Programming languages','Java','galit aharon','Done',60,'5600','dont affect final grade','take your time','05'),(NULL,'020206','Programming languages','Java','galit aharon','Active',60,'1758','second quiz','should be easy by now','06'),(NULL,'020309','Programming languages','C','riki green','Inactive',90,'1300','first quiz','good luck!!!!','09'),(NULL,'020310','Programming languages','C','riki green','Inactive',60,'6672','seocond quiz','','10'),(NULL,'030104','Physics','Mechanics','alon aloni','Inactive',120,'2963','check with ease','GOOD LUCK!','04'),(NULL,'030111','Physics','Mechanics','alon aloni','Inactive',90,'1557','popup quiz','GOOD MORNING','11'),(NULL,'030208','Physics','Electricity','ilana hadar','Inactive',120,'5672','high level test','READ ALL THE ANSWERS','08');
/*!40000 ALTER TABLE `tests` ENABLE KEYS */;
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

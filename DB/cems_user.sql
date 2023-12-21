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
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `userName` varchar(15) NOT NULL,
  `password` varchar(15) NOT NULL,
  `userType` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `userID` varchar(45) NOT NULL,
  `connectionStatus` varchar(45) NOT NULL,
  PRIMARY KEY (`userName`,`password`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('depHead1','123456','math_dep_head','lital','lechsinky','mail1@','101','LoggedOut'),('depHead2','123456','programming_dep_head','eylon','musk','mail15@','102','LoggedOut'),('depHead3','123456','physics_dep_head','Ariel','Shalom','mail12@','103','LoggedOut'),('lecturer1','123456','lecturer','yael','kanter','mail2@','201','LoggedOut'),('lecturer2','123456','lecturer','iris','kanter','mail3@','202','LoggedOut'),('lecturer3','123456','lecturer','alon','aloni','mail13@','203','LoggedOut'),('lecturer4','123456','lecturer','ilana','hadar','mail14@','204','LoggedOut'),('lecturer5','123456','lecturer','galit','aharon','mail16@','205','LoggedOut'),('lecturer6','123456','lecturer','riki','green','mail17@','206','LoggedOut'),('student1','123456','student','roi','darom','mail4@','301','LoggedOut'),('student2','123456','student','yoav','katz','mail5@','302','LoggedOut'),('student3','123456','student','ido','levy','mail6@','303','LoggedOut'),('student4','123456','student','lea','cohen','mail7@','304','LoggedOut'),('student5','123456','student','eyal','gold','mail8@','305','LoggedOut'),('student6','123456','student','ayelet','shalom','mail9@','306','LoggedOut'),('student7','123456','student','ohad','cohen','mail10@','307','LoggedOut'),('student8','123456','student','inbar','israel','mail11@','308','LoggedOut');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
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

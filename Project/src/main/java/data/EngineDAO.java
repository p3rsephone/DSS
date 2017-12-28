package data;

import business.Engine;
import business.courses.Exchange;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class EngineDAO extends DAO implements Map<Integer, Exchange>{
    private Connection conn;

    @Override
    public int size() {
        return size("Exchange");
    }

    @Override
    public boolean isEmpty() {
        return isEmpty("Exchange");
    }

    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try {
            conn = Connect.connect();
            String sql = "SELECT Exchange_code FROM Ups.Exchange WHERE Exchange_bool=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(key.toString()));
            ResultSet rs = ps.executeQuery();
            r = rs.next();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Exchange get(Object key) {
        Exchange exchange = null;
        try {
            conn = Connect.connect();
            String sql = "SELECT * FROM Ups.Exchange WHERE Exchange_code=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, (Integer)key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exchange = new Exchange(rs.getInt("Exchange_code"),rs.getString("Course_code"),rs.getString("Shift_origin"), rs.getString("Shift_dest"),rs.getInt("Student_origin"), rs.getInt("Student_dest"));
                exchange.setCancelled(rs.getBoolean("Exchange_bool"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return exchange;
    }

    @Override
    public Exchange put(Integer key, Exchange value) {
        Exchange exchange = null;
        try {
            conn = Connect.connect();
            String sql = "INSERT INTO Ups.Exchange\n" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE Exchange_bool=VALUES(Exchange_bool), Shift_dest=VALUES(Shift_dest), Shift_origin=VALUES(Shift_origin), Course_code=VALUES(Course_code), Student_dest=VALUES(Student_dest), Student_origin=VALUES(Student_origin);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, value.getCode());
            ps.setBoolean(2, value.isCancelled());
            ps.setString(3, value.getOriginShift());
            ps.setString(4, value.getDestShift());
            ps.setString(5, value.getCourse());
            ps.setInt(6, value.getOriginStudent());
            ps.setInt(7, value.getDestStudent());
            ps.executeUpdate();

            exchange = value;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return exchange;
    }

    @Override
    public Exchange remove(Object key) {
        Exchange exchange = this.get(key);
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Ups.Exchange WHERE Exchange_code = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(key.toString()));
            ps.executeUpdate();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return exchange;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Exchange> m) {
        for(Exchange s : m.values()) {
            put(s.getCode(), s);
        }
    }

    @Override
    public void clear() {
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Ups.Exchange;";
            Statement stm = conn.createStatement();
            stm.executeUpdate(sql);
        } catch (Exception e) {
            //runtime exception!
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> set = null;
        try {
            conn = Connect.connect();
            set = new HashSet<>();
            String sql = "SELECT Exchange_code FROM Ups.Exchange";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                set.add(rs.getInt("Exchange_code"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Connect.close(conn);
        }
        return set;
    }

    @Override
    public Collection<Exchange> values() {
        Collection<Exchange> collection = new HashSet<>();
        try {
            conn = Connect.connect();
            String sql = "SELECT Exchange_code FROM Ups.Exchange";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                Exchange exchange = get(rs.getInt("Exchange_code"));
                collection.add(exchange);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Connect.close(conn);
        }
        return collection;
    }

    @Override
    public Set<Entry<Integer, Exchange>> entrySet() {
        Set<Integer> keys = new HashSet<>(this.keySet());

        HashMap<Integer, Exchange> map = new HashMap<>();
        for (Integer key : keys) {
            map.put(key, this.get(key));
        }
        return map.entrySet();
    }

    public HashMap<Integer, Exchange> list() {
        HashMap<Integer, Exchange> map = new HashMap<>();
        try {
            conn = Connect.connect();
            String sql = "SELECT Exchange_code FROM Ups.Exchange";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Exchange e = get(rs.getInt("Exchange_code"));
                map.put(e.getCode(), e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return map;
    }

    public Set<Exchange> getAllExchangesCourse(Object key) {
        Set<Exchange> collection = new HashSet<>();
        try {
            conn = Connect.connect();
            String sql = "SELECT Exchange_code FROM Ups.Exchange WHERE Course_code=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, (String)key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Exchange exchange = get(rs.getInt("Exchange_code"));
                collection.add(exchange);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Connect.close(conn);
        }
        return collection;
    }

    public void addForeignKeys() {
        try {
            conn = Connect.connect();
            String sql = "ALTER TABLE Ups.StudentShift\n" +
                    "ADD CONSTRAINT `fk_Student_has_Shift_Shift1`  FOREIGN KEY (`Shift_code`)\n" +
                    "REFERENCES `Ups`.`Shift` (`Shift_code`);";
            Statement stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql = "ALTER TABLE Ups.StudentShift\n" +
                    "ADD CONSTRAINT `fk_Student_has_Shift_Student`  FOREIGN KEY (`Student_number`)\n" +
                    "REFERENCES `Ups`.`Student` (`Student_number`);";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql = "ALTER TABLE Ups.Course\n" +
                    "ADD CONSTRAINT `fk_Course_Teacher1`  FOREIGN KEY (`Teacher_number`)\n" +
                    "REFERENCES `Ups`.`Teacher` (`Teacher_number`);";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql = "ALTER TABLE Ups.Request\n" +
                    "ADD CONSTRAINT `fk_Request_Course1`  FOREIGN KEY (`Course_code`)\n" +
                    "REFERENCES `Ups`.`Course` (`Course_code`);";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql = "ALTER TABLE Ups.RequestStudent\n" +
                    "ADD CONSTRAINT `fk_Request_has_Student_Request1`  FOREIGN KEY (`Request_code`)\n" +
                    "REFERENCES `Ups`.`Request` (`Request_code`);";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql = "ALTER TABLE Ups.RequestStudent\n" +
                    "ADD CONSTRAINT `fk_Request_has_Student_Student1`  FOREIGN KEY (`Student_number`)\n" +
                    "REFERENCES `Ups`.`Student` (`Student_number`);";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql = "ALTER TABLE Ups.Shift\n" +
                    "ADD CONSTRAINT `fk_Shift_Course1`  FOREIGN KEY (`Course_code`)\n" +
                    "REFERENCES `Ups`.`Course` (`Course_code`);";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql = "ALTER TABLE Ups.Shift\n" +
                    "ADD CONSTRAINT `fk_Shift_Room1`  FOREIGN KEY (`Room_code`)\n" +
                    "REFERENCES `Ups`.`Room` (`Room_code`);";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql = "ALTER TABLE Ups.Shift\n" +
                    "ADD CONSTRAINT `fk_Shift_Teacher1`  FOREIGN KEY (`Teacher_number`)\n" +
                    "REFERENCES `Ups`.`Teacher` (`Teacher_number`);";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql = "ALTER TABLE Ups.StudentCourse\n" +
                    "ADD CONSTRAINT `fk_Student_has_Course_Course1`  FOREIGN KEY (`Course_code`)\n" +
                    "REFERENCES `Ups`.`Course` (`Course_code`);";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql = "ALTER TABLE Ups.StudentCourse\n" +
                    "ADD CONSTRAINT `fk_Student_has_Course_Student1`  FOREIGN KEY (`Student_number`)\n" +
                    "REFERENCES `Ups`.`Student` (`Student_number`);";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql = "ALTER TABLE Ups.Exchange\n" +
                    "ADD CONSTRAINT `fk_Exchange_Shift1`  FOREIGN KEY (`Shift_origin`)\n" +
                    "REFERENCES `Ups`.`Shift` (`Shift_code`);";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql = "ALTER TABLE Ups.Exchange\n" +
                    "ADD CONSTRAINT `fk_Exchange_Shift2`  FOREIGN KEY (`Shift_dest`)\n" +
                    "REFERENCES `Ups`.`Shift` (`Shift_code`);";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql = "ALTER TABLE Ups.Exchange\n" +
                    "ADD CONSTRAINT `fk_Exchange_Course1`  FOREIGN KEY (`Course_code`)\n" +
                    "REFERENCES `Ups`.`Course` (`Course_code`);";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql = "ALTER TABLE Ups.Exchange\n" +
                    "ADD CONSTRAINT `fk_Exchange_Student1`  FOREIGN KEY (`Student_origin`)\n" +
                    "REFERENCES `Ups`.`Student` (`Student_number`);";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql = "ALTER TABLE Ups.Exchange\n" +
                    "ADD CONSTRAINT `fk_Exchange_Student2`  FOREIGN KEY (`Student_dest`)\n" +
                    "REFERENCES `Ups`.`Student` (`Student_number`);";
            stm = conn.createStatement();
            stm.executeUpdate(sql);


        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
    }

    public Integer getNPhase() {
        Integer phase = 1;
        try {
            conn = Connect.connect();
            String sql = "SELECT Engine_phase FROM Ups.Engine WHERE Engine_code=1;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                phase = rs.getInt("Engine_phase");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return phase;
    }

    public Integer getNRequests() {
        Integer requests = 0;
        try {
            conn = Connect.connect();
            String sql = "SELECT Engine_requests FROM Ups.Engine WHERE Engine_code=1;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                requests = rs.getInt("Engine_requests");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return requests;
    }

    public Integer getNExchanges() {
        Integer exchanges = 0;
        try {
            conn = Connect.connect();
            String sql = "SELECT Engine_exchanges FROM Ups.Engine WHERE Engine_code=1;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exchanges = rs.getInt("Engine_exchanges");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return exchanges;
    }

    public void putEngine(Engine value) {
        try {
            conn = Connect.connect();
            String sql = "INSERT INTO Ups.Engine\n" +
                    "VALUES (1, ?, ?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE Engine_exchanges=VALUES(Engine_exchanges), Engine_phase=VALUES(Engine_phase), Engine_requests=VALUES(Engine_requests);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, value.getNrOfExchanges());
            ps.setInt(2, value.getNrOfRequests());
            ps.setInt(3, value.getPhase());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
    }

    public void reset() {
        try {
            conn = Connect.connect();
            String sql = "DROP SCHEMA Ups";
            Statement stm = conn.createStatement();
            stm.executeUpdate(sql);
            create();
        } catch (Exception e) {
            //runtime exception!
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
    }

    public void create() {
        try {
            conn = Connect.connect();
            String sql = "SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;";
            Statement stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="CREATE SCHEMA IF NOT EXISTS `Ups` DEFAULT CHARACTER SET utf8 ;\n";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="USE `Ups` ;\n";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="CREATE TABLE IF NOT EXISTS `Ups`.`Teacher` (\n" +
                    "  `Teacher_number` INT(11) NOT NULL,\n" +
                    "  `Teacher_name` VARCHAR(45) NOT NULL,\n" +
                    "  `Teacher_email` VARCHAR(45) NOT NULL,\n" +
                    "  `Teacher_password` VARCHAR(45) NOT NULL,\n" +
                    "  `Teacher_isBoss` TINYINT(4) NOT NULL,\n" +
                    "  PRIMARY KEY (`Teacher_number`))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;\n";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="CREATE TABLE IF NOT EXISTS `Ups`.`Course` (\n" +
                    "  `Course_code` VARCHAR(45) NOT NULL,\n" +
                    "  `Course_name` VARCHAR(45) NOT NULL,\n" +
                    "  `Course_year` INT(11) NOT NULL,\n" +
                    "  `Teacher_number` INT(11) NOT NULL,\n" +
                    "  PRIMARY KEY (`Course_code`),\n" +
                    "  UNIQUE INDEX `code_UNIQUE` (`Course_code` ASC),\n" +
                    "  INDEX `fk_Course_Teacher1_idx` (`Teacher_number` ASC))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;\n";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="CREATE TABLE IF NOT EXISTS `Ups`.`Request` (\n" +
                    "  `Request_code` INT(11) NOT NULL,\n" +
                    "  `Request_originShift` VARCHAR(45) NOT NULL,\n" +
                    "  `Request_destShift` VARCHAR(45) NOT NULL,\n" +
                    "  `Course_code` VARCHAR(45) NOT NULL,\n" +
                    "  PRIMARY KEY (`Request_code`),\n" +
                    "  INDEX `fk_Request_Course1_idx` (`Course_code` ASC))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;\n";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="CREATE TABLE IF NOT EXISTS `Ups`.`Student` (\n" +
                    "  `Student_number` INT(11) NOT NULL,\n" +
                    "  `Student_name` VARCHAR(45) NOT NULL,\n" +
                    "  `Student_email` VARCHAR(45) NOT NULL,\n" +
                    "  `Student_password` VARCHAR(45) NOT NULL,\n" +
                    "  `Student_statute` TINYINT(4) NOT NULL DEFAULT '0',\n" +
                    "  PRIMARY KEY (`Student_number`),\n" +
                    "  UNIQUE INDEX `number_UNIQUE` (`Student_number` ASC))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;\n";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="CREATE TABLE IF NOT EXISTS `Ups`.`RequestStudent` (\n" +
                    "  `Request_code` INT(11) NOT NULL,\n" +
                    "  `Student_number` INT(11) NOT NULL,\n" +
                    "  PRIMARY KEY (`Request_code`, `Student_number`),\n" +
                    "  INDEX `fk_Request_has_Student_Student1_idx` (`Student_number` ASC),\n" +
                    "  INDEX `fk_Request_has_Student_Request1_idx` (`Request_code` ASC))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;\n" ;
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="CREATE TABLE IF NOT EXISTS `Ups`.`Room` (\n" +
                    "  `Room_code` VARCHAR(45) NOT NULL,\n" +
                    "  `Room_capacity` INT(11) NOT NULL,\n" +
                    "  PRIMARY KEY (`Room_code`))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;\n";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="CREATE TABLE IF NOT EXISTS `Ups`.`Shift` (\n" +
                    "  `Shift_code` VARCHAR(45) NOT NULL,\n" +
                    "  `Shift_numOfStudents` INT(11) NOT NULL,\n" +
                    "  `Shift_limit` INT(11) NOT NULL,\n" +
                    "  `Shift_expectedClasses` INT(11) NOT NULL,\n" +
                    "  `Shift_givenClasses` INT(11) NOT NULL,\n" +
                    "  `Shift_weekday` VARCHAR(45) NOT NULL,\n" +
                    "  `Shift_period` VARCHAR(45) NOT NULL,\n" +
                    "  `Course_code` VARCHAR(45) NOT NULL,\n" +
                    "  `Room_code` VARCHAR(45) NOT NULL,\n" +
                    "  `Teacher_number` INT(11) NOT NULL,\n" +
                    "  PRIMARY KEY (`Shift_code`),\n" +
                    "  UNIQUE INDEX `code_UNIQUE` (`Shift_code` ASC),\n" +
                    "  INDEX `fk_Shift_Course1_idx` (`Course_code` ASC),\n" +
                    "  INDEX `fk_Shift_Room1_idx` (`Room_code` ASC),\n" +
                    "  INDEX `fk_Shift_Teacher1_idx` (`Teacher_number` ASC))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;\n";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql= "CREATE TABLE IF NOT EXISTS `Ups`.`StudentCourse` (\n" +
                    "  `Student_number` INT(11) NOT NULL,\n" +
                    "  `Course_code` VARCHAR(45) NOT NULL,\n" +
                    "  PRIMARY KEY (`Student_number`, `Course_code`),\n" +
                    "  INDEX `fk_Student_has_Course_Course1_idx` (`Course_code` ASC),\n" +
                    "  INDEX `fk_Student_has_Course_Student1_idx` (`Student_number` ASC))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;\n";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="CREATE TABLE IF NOT EXISTS `Ups`.`StudentShift` (\n" +
                    "  `Student_number` INT(11) NOT NULL,\n" +
                    "  `Shift_code` VARCHAR(45) NOT NULL,\n" +
                    "  `StudentShift_absences` INT(11) NOT NULL DEFAULT 0,\n" +
                    "  PRIMARY KEY (`Student_number`, `Shift_code`),\n" +
                    "  INDEX `fk_Student_has_Shift_Shift1_idx` (`Shift_code` ASC),\n" +
                    "  INDEX `fk_Student_has_Shift_Student_idx` (`Student_number` ASC))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;\n";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="CREATE TABLE IF NOT EXISTS `Ups`.`Engine` (\n" +
                    "  `Engine_code` INT(11) NOT NULL,\n" +
                    "  `Engine_exchanges` INT(11) NOT NULL,\n" +
                    "  `Engine_requests` INT(11) NOT NULL,\n" +
                    "  `Engine_phase` INT(11) NOT NULL,\n" +
                    "  PRIMARY KEY (`Engine_code`))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;\n";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="CREATE TABLE IF NOT EXISTS `Ups`.`Exchange` (\n" +
                    "  `Exchange_code` INT NOT NULL,\n" +
                    "  `Exchange_bool` TINYINT NOT NULL,\n" +
                    "  `Shift_origin` VARCHAR(45) NOT NULL,\n" +
                    "  `Shift_dest` VARCHAR(45) NOT NULL,\n" +
                    "  `Course_code` VARCHAR(45) NOT NULL,\n" +
                    "  `Student_origin` INT(11) NOT NULL,\n" +
                    "  `Student_dest` INT(11) NOT NULL,\n" +
                    "  PRIMARY KEY (`Exchange_code`),\n" +
                    "  INDEX `fk_Exchange_Shift1_idx` (`Shift_origin` ASC),\n" +
                    "  INDEX `fk_Exchange_Shift2_idx` (`Shift_dest` ASC),\n" +
                    "  INDEX `fk_Exchange_Course1_idx` (`Course_code` ASC),\n" +
                    "  INDEX `fk_Exchange_Student1_idx` (`Student_origin` ASC),\n" +
                    "  INDEX `fk_Exchange_Student2_idx` (`Student_dest` ASC))\n" +
                    "ENGINE = InnoDB;\n";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="SET SQL_MODE=@OLD_SQL_MODE;\n";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;\n";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
            sql="SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;";
            stm = conn.createStatement();
            stm.executeUpdate(sql);
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
    }

    public boolean exists() {
        boolean ret = false;
        try {
            conn = Connect.connect();
            String sql = "SELECT * FROM INFORMATION_SCHEMA.SCHEMATA";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                if (rs.getString("SCHEMA_NAME").equals("Ups")) ret = true;
            }
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
        Connect.close(conn);
    }

        return ret;
    }
}

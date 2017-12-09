Sempre que se fizer um connect fazer um close uma vez que a poll de connection pode esgotar e todos os objetos carregados sao perdidos


nao fazer coisas assim (vulneraveia a sql injections)

  public void save(User){
      String sql = "insert into 'User' ('email', passwword') values" + u.getEmail() + "," + u.getPassword() + ",");
    }

    basta meter no form do user
      '; drop table 'User';
    para apagar a table


Exemplo de DAO

  podemos ter uma class DAO e um USERDAO que utiliza a classe por exemplo
      podemos ter uma class DAO e um USERDAO que utiliza a classe por exemplo
        UserDAO tem um sizeAluno que uitliza o metodo size do DAO que recebe o nome da tabela


É necessario ter o mariadb driver , pode ser encontrado aqui[https://downloads.mariadb.org/connector-java/]
e chamado como org.mariadb.jdbc.Driver e nao como com.mariadb.jdbc.Driver

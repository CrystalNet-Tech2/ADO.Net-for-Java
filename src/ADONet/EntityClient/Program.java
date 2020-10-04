package ADONet.EntityClient;

import system.Console;
import system.Enum;
import system.data.CommandBehavior;
import system.data.common.DbDataReader;
import system.data.entityclient.*;

/*
The code in this example uses an EntityCommand to execute an Entity SQL query. This query returns a list of records that represent instances of the Categories entity type. An EntityDataReader is used to access data records in the result set. For more information, see EntityClient Provider for the Entity Framework.
 */
public class Program {

    public static void main(String[] args) {

        String queryString = "SELECT c.CategoryID, c.CategoryName FROM NorthwindEntities.Categories AS c";

        try {
            EntityConnection conn = new EntityConnection("name=NorthwindEntities");
            try {
                conn.Open();
                EntityCommand query = new EntityCommand(queryString, conn);
                try {
                    DbDataReader rdr = query.ExecuteReader(Enum.of(CommandBehavior.SequentialAccess));
                    try {
                        while (rdr.Read()) {
                            Console.WriteLine("\t{0}\t{1}", rdr.get_Item(0), rdr.get_Item(1));
                        }
                    } finally {
                        rdr.Dispose();
                        rdr.close();
                    }
                } finally {
                    query.Dispose();
                    query.close();
                }
            } catch (Exception ex) {

                Console.WriteLine(ex.getMessage());
            } finally {
                conn.Dispose();
                conn.close();
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}
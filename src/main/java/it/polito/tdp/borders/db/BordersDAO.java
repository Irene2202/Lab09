package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void loadAllCountries(Map<Integer, Country> map) {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(!map.containsKey(rs.getInt("ccode"))) {
					Country c=new Country(rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
					map.put(c.getcCode(), c);
				}
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	

	public List<Border> getCountryPairs(Map<Integer, Country> map,int anno) {
		String sql="SELECT c.state1no AS id1, c.state2no AS id2, c.year, c.conttype "
				+"FROM contiguity c "
				+"WHERE c.state1no>c.state2no AND c.year<=? AND c.conttype=1";
		
		List<Border> result=new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Country c1=map.get(rs.getInt("id1"));
				Country c2=map.get(rs.getInt("id2"));
				
				if(c1!=null && c2!=null) {
					result.add(new Border(c1, c2, rs.getInt("c.year"), rs.getInt("c.conttype")));
				}
				else {
					System.out.println("Errore Borders");
				}
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Errore DB", e);
		}

	}


	public List<Country> getVertici(Map<Integer, Country> idMap, int anno) {
		String sql="SELECT c.state1no AS id_vertice "
				+"FROM contiguity c "
				+"WHERE c.year<=? AND conttype=1 "
				+"GROUP BY c.state1no";
		
		List<Country> result=new LinkedList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(idMap.get(rs.getInt("id_vertice")));
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			throw new RuntimeException("Errore DB", e);
		}

	}
}

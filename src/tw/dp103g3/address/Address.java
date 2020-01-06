package tw.dp103g3.address;

public class Address {
	private int id;
	private int mem_id;
	private String name;
	private String info;
	private byte state;
	private double latitude;
	private double longitude;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result += prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result += prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || !(obj instanceof Address)) {
			return false;
		}
		Address address = (Address) obj;
		return address.getLatitude() == this.getLatitude() && address.getLongitude() == this.getLongitude();
	}

	public Address(int id, int mem_id, String name, String info, byte state, double latitude, double longitude) {
		this.id = id;
		this.mem_id = mem_id;
		this.name = name;
		this.info = info;
		this.state = state;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Address(int id, String name, String info, double latitude, double longitude) {
		super();
		this.id = id;
		this.name = name;
		this.info = info;
		this.latitude = latitude;
		this.longitude = longitude;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMem_id() {
		return mem_id;
	}

	public void setMem_id(int mem_id) {
		this.mem_id = mem_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
}
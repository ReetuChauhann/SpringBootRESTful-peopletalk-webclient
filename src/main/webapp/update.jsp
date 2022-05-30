<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>

<h3 style="background:orange; color: black; ">Update Here</h3>
<body>
       <p style="background:orange; color: black; "> ${status} </p> 
		<form action="Updateprofile" method="post" enctype="multipart/form-data">
		Email :: <input type="text" name="email" placeholder="EMAIL" required/> <br> <br>
		Name :: <input type="text" name="name" placeholder="NAME" required/> <br> <br>
		Phone :: <input type="text" name="phone" placeholder="Phone" required/> <br> <br>
		Gender :: <input type="radio" name="gender" value="male"> Male <input type="radio" name="gender" value="female"> Female <br> <br>
		DOB :: <input type="text" name="dob" placeholder="DATE OF BIRTH" required/> <br> <br>
		State :: <input type="text" name="state" placeholder="State" required/> <br> <br>
		City :: <input type="text" name="city" placeholder="City" required/> <br> <br>
		Area :: <input type="text" name="area" placeholder="Area" required/> <br> <br>
		Image :: <input type="file" name="image" > <br> <br>
	    Confirm Password :: <input type="password" name="password" placeholder="Password" required/> <br> <br>
		 <button>Update</button>
		</form>
      
</body>
</html>
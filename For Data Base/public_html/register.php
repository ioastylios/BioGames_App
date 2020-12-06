<?php
	include("config.php");

	$firstname = $_REQUEST("firstname");
	$lastname = $_REQUEST("lastname");
	$contact_no = $_REQUEST("contact_no");
	$email = $_REQUEST("email");
	$password = $_REQUEST("password");
	$country = $_REQUEST("country");
	$state = $_REQUEST("state");
	$city = $_REQUEST("city");
	$postcode = $_REQUEST("postcode");
	$latitude = $_REQUEST("latitude");
	$longitude = $_REQUEST("longitude");

	$query = "select * from user_tb where email='$email'";
	$result = mysql_query($query);

	if (@mysql_num_rows($result) != 0) {
		echo json_encode(array("success":"0", "msg":"Someone already has this email address. Try another name."));
	}
	else {
		$query = "select max(user_id) as maxid from user_tb";
		$result = mysql_query($query);
		$row = @mysql_fetch_object($result);
		$maxid = $row->maxid + 1;

		$query = "insert into user_tb (user_id, firstname, lastname, email, contact_no, password, country, state, city, postcode, latitude, longitude, reg_date, access_time) values ('$maxid', '$firstname', '$lastname', '$email', '$contact_no', '$password', '$country', '$state', '$city', '$postcode', '$latitude', '$longitude', NOW(), NOW())";
		if (mysql_query($query)) {
			$query = "select * from user_tb where user_id='$maxid'";
			$result = mysql_query($query);
			$row = @mysql_fetch_object($result);
			
			sendMail($email);
			echo json_encode(array("success":"1", "userinfo":$row));
		}
		else {
			echo json_encode(array("success":"0", "msg":"Failed."));
		}
	}

	function sendMail($to) {
		$subject = 'Find A Vendor';
		$from = 'findvendor@support.com';
		 
		// To send HTML mail, the Content-type header must be set
		$headers  = 'MIME-Version: 1.0' . "\r\n";
		$headers .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
		 
		// Create email headers
		$headers .= 'From: '.$from."\r\n".
			'Reply-To: '.$from."\r\n" .
			'X-Mailer: PHP/' . phpversion();
		 
		// Compose a simple HTML email message
		$message = '<html><body>';
		$message .= '<h1 style="color:#f40;">Hi!</h1>';
		$message .= '<p style="color:#080;font-size:18px;">Thanks for your vising my app. If you have any questions, please response this email.</p>';
		$message .= '</body></html>';
		 
		// Sending email
		if(mail($to, $subject, $message, $headers)){
			//echo 'Your mail has been sent successfully.';
		} else{
			//echo 'Unable to send email. Please try again.';
		}
	}
?>    
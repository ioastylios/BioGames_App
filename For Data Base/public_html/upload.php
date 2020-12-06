<?php

	$latitude = $_REQUEST["latitude"];
	$longitude = $_REQUEST["longitude"];
	$title = $_REQUEST["title"];
	$snippet = $_REQUEST["snippet"];

	$response = array(   
		'code' => "400",    
        'msg' => ""
    );

	$validextensions = array("jpeg", "jpg", "png");
	$temporary = explode(".", $_FILES["file"]["name"]);
	$file_extension = end($temporary);

	if ((($_FILES["file"]["type"] == "image/png") || ($_FILES["file"]["type"] == "image/jpg") || ($_FILES["file"]["type"] == "image/jpeg")) 
		&& ($_FILES["file"]["size"] < 500000) && in_array($file_extension, $validextensions)) {

		if ($_FILES["file"]["error"] > 0) {
			$response['msg'] = "Return Code: " . $_FILES["file"]["error"];
		} else {

			$date = date('Y-m-d H-i-s', time());
			$ext = strrchr($_FILES["file"]["type"],'/');
			$filename = $date.".".$ext;
			
			if (file_exists("upload/" . $_FILES["file"]["name"])) {
				$response['msg'] = $_FILES["file"]["name"] . " already exists.";
			} else {
				move_uploaded_file($_FILES["file"]["tmp_name"], "upload/" . $filename);

			}

		}
	} else {
		$response['msg'] = "Invalid file Size or Type.";
	}
?>
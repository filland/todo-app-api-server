<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Registration confirmation</title>
    <style>
        .main {
            margin: 20px;
        }

        h2 {
            padding-top: 0;
            padding-bottom: 0;
            margin-top: 0;
            margin-bottom: 15px;
        }

        p {
            margin-top: 0;
            margin-bottom: 5px;
        }
    </style>
</head>
<body>
<div class="main">
    <h2>Welcome to TodoApp!</h2>
    <p>
        To complete your registration please click the link below
    </p>
    <p>
        Link to finish the registration - ${confirmationLink}
    </p>
</div>
</body>
</html>
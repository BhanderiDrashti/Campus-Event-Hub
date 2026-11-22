<!DOCTYPE html>
<html>
<head>

    <title>Admin Login - Campus Event Hub</title>

    <style>

        body {
            margin: 0;
            padding: 0;
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
        }

        .login-container {
            width: 400px;
            margin: 100px auto;
            background-color: white;
            padding: 35px;
            border-radius: 12px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.15);
        }

        h1 {
            text-align: center;
            color: #2c3e50;
            margin-bottom: 10px;
        }

        .subtitle {
            text-align: center;
            color: #777;
            margin-bottom: 25px;
        }

        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
            color: #333;
        }

        input {
            width: 100%;
            padding: 12px;
            margin-top: 7px;
            border: 1px solid #ccc;
            border-radius: 6px;
            box-sizing: border-box;
        }

        button {
            width: 100%;
            padding: 12px;
            margin-top: 25px;
            border: none;
            border-radius: 6px;
            background-color: #2c3e50;
            color: white;
            font-size: 16px;
            cursor: pointer;
        }

        button:hover {
            background-color: #1a252f;
        }

        .back {
            display: block;
            text-align: center;
            margin-top: 20px;
            text-decoration: none;
            color: #2c3e50;
        }

    </style>

</head>

<body>

    <div class="login-container">

        <h1>Admin Login</h1>

        <p class="subtitle">
            Login to manage campus events
        </p>

        <form action="adminLogin" method="post">

            <label>Username</label>

            <input type="text"
                   name="username"
                   placeholder="Enter admin username"
                   required>


            <label>Password</label>

            <input type="password"
                   name="password"
                   placeholder="Enter admin password"
                   required>


            <button type="submit">
                Login
            </button>

        </form>

        <a href="index.html" class="back">
            ? Back to Home
        </a>

    </div>

</body>
</html>
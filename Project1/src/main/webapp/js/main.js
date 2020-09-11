const APP_VIEW = document.getElementById('app-view');

window.onload = function(){ //function w/out a name = anon function
	loadLogin();
//	loadRegister();
	document.getElementById('toLogin').addEventListener('click', loadLogin);
	document.getElementById('toRegister').addEventListener('click', loadRegister);
	document.getElementById('toHome').addEventListener('click', loadHome);
	document.getElementById('toLogout').addEventListener('click', logout);
}

//#region loaders

function loadPage(page){
	console.log(page);
	let xhr = new XMLHttpRequest;
	// xhr.responseType();
	xhr.open("GET", page, true);
	xhr.send();
	xhr.onreadystatechange = function(){
		if(xhr.readyState == 4 && xhr.status == 200){
			// console.log('response received');
			APP_VIEW.innerHTML = xhr.responseText;
		}
	}
}

function loadLogin(){
	console.log('in loadLogin()');
	let xhr = new XMLHttpRequest;
	// xhr.responseType();
	xhr.open("GET", "login.view", true); // third parameter of this method is optional (defaults to true)
	xhr.send();
	xhr.onreadystatechange = function(){
		if(xhr.readyState == 4 && xhr.status == 200){
			// console.log('response received');
			APP_VIEW.innerHTML = xhr.responseText;
			configureLoginView();
		}
	}
}

function loadRegister(){
	console.log('in loadRegister()');
	let xhr = new XMLHttpRequest;
	// xhr.responseType();
	xhr.open("GET", "register.view"); // third parameter of this method is optional (defaults to true)
	xhr.send();
	xhr.onreadystatechange = function(){
		if(xhr.readyState == 4 && xhr.status == 200){
			// console.log('response received');
			APP_VIEW.innerHTML = xhr.responseText;
			configureRegisterView();
		}
	}
}

function loadHome(){
	console.log('in loadHome()');
	if(!localStorage.getItem('authUser')){
		console.log('No user logged in, navigating to login screen');
		loadLogin();
		return;
	}
	let xhr = new XMLHttpRequest;
	// xhr.responseType();
	xhr.open("GET", "home.view", true); 
	xhr.send(); 
	xhr.onreadystatechange = function(){
		if(xhr.readyState == 4 && xhr.status == 200){
			// console.log('response received');
			APP_VIEW.innerHTML = xhr.responseText;
			configureHomeView();
		}
	}
}

function loadLogout(){
	console.log('in loadLogout()');
	let xhr = new XMLHttpRequest;
	// xhr.responseType();
	xhr.open("GET", "logout.view", true);
	xhr.send();
	xhr.onreadystatechange = function(){
		if(xhr.readyState == 4 && xhr.status == 200){
			// console.log('response received');
			APP_VIEW.innerHTML = xhr.responseText;
		}
	}
}

//#endregion

//#region configurations

function configureLoginView(){
	console.log('in configureLoginView');

	document.getElementById('login-message').setAttribute('hidden', true);
	document.getElementById('login-button-container').addEventListener('mouseover', validateLoginForm);
	document.getElementById('login').addEventListener('click', login);
}

function configureRegisterView(){
    console.log('in configureRegisterView()');

    document.getElementById('reg-message').setAttribute('hidden', true);

    document.getElementById('reg-username').addEventListener('blur', isUsernameAvailable);
    document.getElementById('email').addEventListener('blur', isEmailAvailable);

    document.getElementById('register').setAttribute('disabled', true);
    document.getElementById('reg-button-container').addEventListener('mouseover', validateRegisterForm);
    document.getElementById('register').addEventListener('click', register);
}

function configureHomeView(){
	console.log('in configureHomeView');

	let authUser = JSON.parse(localStorage.getItem('authUser'));
	document.getElementById('loggedInUsername').innerText = authUser.username;
}

//#endregion

//#region operations

function login(){
	console.log('in login()');

	let un = document.getElementById('login-username').value;
	let pw = document.getElementById('login-password').value;

	let credentials = {
		username: un,
		password: pw
	}

	let credentialsJSON = JSON.stringify(credentials);

	let xhr = new XMLHttpRequest;
	xhr.open('POST', 'auth');
	xhr.setRequestHeader('Content-type', 'application/json');
	xhr.send(credentialsJSON);

	xhr.onreadystatechange = function(){
		if (xhr.readyState == 4 && xhr.status == 200){

			console.log('login successful!');
			document.getElementById('login-message').setAttribute('hidden', true);
			localStorage.setItem('authUser', xhr.responseText);
			loadHome();

		} else if (xhr.readyState == 4 && xhr.status == 401){

			document.getElementById('login-message').removeAttribute('hidden');
			let err = JSON.parse(xhr.responseText);
			document.getElementById('login-message').innerText = err.message;

		}
	}

}

function register(){
	console.log('in register()');

	let fn = document.getElementById('fn').value;
	let ln = document.getElementById('ln').value;
	let em = document.getElementById('email').value;
	let un = document.getElementById('reg-username').value;
	let pw = document.getElementById('reg-password').value;

	let credentials = {
		email:em,
		firstName:fn,
		lastName:ln,
		username: un,
		password: pw
	}

	let credentialsJSON = JSON.stringify(credentials);

	let xhr = new XMLHttpRequest;
	xhr.open('POST', 'users');
	xhr.setRequestHeader('Content-type', 'application/json');
	xhr.send(credentialsJSON);

	xhr.onreadystatechange = function(){
		if (xhr.readyState == 4 && xhr.status == 201){

			let msg = JSON.parse(xhr.responseText);
			console.log(msg);
			// document.getElementById('reg-message').innerText = msg;
			document.getElementById('reg-message').setAttribute('hidden', true);
			loadLogin();

		} else if (xhr.readyState == 4 && xhr.status == 400){

			document.getElementById('reg-message').removeAttribute('hidden');
			let err = JSON.parse(xhr.responseText);
			document.getElementById('reg-message').innerText = err.message;

		}
	}
}

function logout() {
    
    console.log('in logout()');

    let xhr = new XMLHttpRequest();

    xhr.open('GET', 'auth');
    xhr.send();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 204) {
			console.log('logout successful!');
			localStorage.removeItem('authUser');
			loadLogin();
        }
    }
}

function isUsernameAvailable() {

    console.log('in isUsernameAvailable()');

    let username = document.getElementById('reg-username').value;

    let xhr = new XMLHttpRequest();

    xhr.open('POST', 'username.validate');
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(JSON.stringify(username));

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 204) {
            console.log('Provided username is available!');
            document.getElementById('reg-message').setAttribute('hidden', true);
        } else if (xhr.readyState == 4 && xhr.status == 409 ) {
            document.getElementById('reg-message').removeAttribute('hidden')
            document.getElementById('reg-message').innerText = 'The provided username is already taken!';
            document.getElementById('register').setAttribute('disabled', true);
        }
    }

}

function isEmailAvailable() {

    console.log('in isEmailAvailable()');

    let email = document.getElementById('email').value;

    let xhr = new XMLHttpRequest();

    xhr.open('POST', 'email.validate');
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(JSON.stringify(email));

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 204) {
            console.log('Provided email is available!');
            document.getElementById('reg-message').setAttribute('hidden', true);
        } else if (xhr.readyState == 4 && xhr.status == 409) {
            document.getElementById('reg-message').removeAttribute('hidden');
            document.getElementById('reg-message').innerText = 'The provided email address is already taken!';
            document.getElementById('register').setAttribute('disabled', true);
        }
    }
}

//#endregion

//#region formValidation

function validateLoginForm() {

    console.log('in validateLoginForm()');

    let msg = document.getElementById('login-message').innerText;

    if (msg == 'User authentication failed!') {
        return;
    }

    let un = document.getElementById('login-username').value;
    let pw = document.getElementById('login-password').value;

    if (!un || !pw) {
        document.getElementById('login-message').removeAttribute('hidden');
        document.getElementById('login-message').innerText = 'You must provided values for all fields in the form!'
        document.getElementById('login').setAttribute('disabled', true);
    } else {
        document.getElementById('login').removeAttribute('disabled');
        document.getElementById('login-message').setAttribute('hidden', true);
    }

}

function validateRegisterForm() {

    console.log('in validateRegisterForm()');

    let fn = document.getElementById('fn').value;
    let ln = document.getElementById('ln').value;
    let email = document.getElementById('email').value;
    let un = document.getElementById('reg-username').value;
    let pw = document.getElementById('reg-password').value;

    if (!fn || !ln || !email || !un || !pw) {
        document.getElementById('reg-message').removeAttribute('hidden');
        document.getElementById('reg-message').innerText = 'You must provided values for all fields in the form!'
        document.getElementById('register').setAttribute('disabled', true);
    } else {
        document.getElementById('register').removeAttribute('disabled');
        document.getElementById('reg-message').setAttribute('hidden', true);
    }
}

//#endregion
const APP_VIEW = document.getElementById('app-view');
const NAV_BAR = document.getElementById('navbar');

window.onload = function(){ //function w/out a name = anon function
	loadView("login.view");
	$('.dropdown-toggle').dropdown();
}

//#region loaders

function loadView(pageToLoad){
	console.log('in ' + pageToLoad);
	if(pageToLoad == 'home.view'){
		if(!localStorage.getItem('authUser')){
			console.log('No user logged in, navigating to login screen');
			loadView("login.view");
			return;
		}
	}
	let xhr = new XMLHttpRequest;
	// xhr.responseType();
	xhr.open("GET", pageToLoad, true); // third parameter of this method is optional (defaults to true)
	xhr.send();
	xhr.onreadystatechange = async function(){
		if(xhr.readyState == 4 && xhr.status == 200){
			APP_VIEW.innerHTML = xhr.responseText;
			switch(pageToLoad){
				default:
					console.log('loading default page');
				case 'login.view':
					configureLoginView();
					break;
				case 'home.view':
					configureHomeView();
					break;
				case 'profile.view':
					configureProfileView();
					break;
				case 'user_register.view':
					configureRegisterView();
					break;
				case 'user_update.view':
				case 'user_view.view':
				case 'user_delete.view':
					configureUsersView();
					break;
				case 'reimbursement_create.view':
				case 'reimbursement_update.view':
				case 'reimbursement_view.view':
					configureReimbursementsView();
					break;
			}
			loadNavBar();
		}
	}
}

function eventLoadView(evt){
	loadView(evt.currentTarget.desiredView);
}

// function loadLogin(){
// 	console.log('in loadLogin()');
// 	let xhr = new XMLHttpRequest;
// 	// xhr.responseType();
// 	xhr.open("GET", "login.view", true); // third parameter of this method is optional (defaults to true)
// 	xhr.send();
// 	xhr.onreadystatechange = async function(){
// 		if(xhr.readyState == 4 && xhr.status == 200){
// 			APP_VIEW.innerHTML = xhr.responseText;
// 			configureLoginView();
// 		}
// 	}
// }

// function loadUserRegister(){
// 	console.log('in loadRegister()');
// 	let xhr = new XMLHttpRequest;
// 	// xhr.responseType();
// 	xhr.open("GET", "user_register.view"); // third parameter of this method is optional (defaults to true)
// 	xhr.send();
// 	xhr.onreadystatechange = function(){
// 		if(xhr.readyState == 4 && xhr.status == 200){
// 			// console.log('response received');
// 			APP_VIEW.innerHTML = xhr.responseText;
// 			configureRegisterView();
// 		}
// 	}
// }

function loadHome(){
	console.log('in loadHome()');
	if(!localStorage.getItem('authUser')){
		console.log('No user logged in, navigating to login screen');
		loadView("login.view");
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

function loadNavBar(){
	console.log('in loadNavBar');
	let xhr = new XMLHttpRequest;
	let destNav = "main.nav";
	let authUser = JSON.parse(localStorage.getItem('authUser'));
	var authRole = "main";
	if(authUser){
		console.log(authUser);
		authRole = authUser.role.toLowerCase();
		destNav = authRole + ".nav";
	}
	console.log(destNav);
	xhr.open("GET", destNav, true);
	xhr.send();
	xhr.onreadystatechange = async function(){
		if(xhr.readyState == 4 && xhr.status == 200){
			NAV_BAR.innerHTML = xhr.responseText;
			switch(authRole){
				default:
				case 'main':
					// configureNavbarUserDropdown();
					document.getElementById('toLogin').addEventListener('click', login);
					break;
				case 'admin':
					configureNavbarUserDropdown();
					configureNavbarReimbursementDropdown();
					configureDefaultViewButtons();
					document.getElementById('toLogout').addEventListener('click', logout);
					break;
				case 'employee':
					configureNavbarReimbursementDropdown();
					configureDefaultViewButtons();
					document.getElementById('toLogout').addEventListener('click', logout);
					break;
				case 'manager':
					configureNavbarReimbursementDropdown();
					configureDefaultViewButtons();
					document.getElementById('toLogout').addEventListener('click', logout);
					break;
			}
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

function configureDefaultViewButtons(){
	console.log('in configureDefaultViewButtons');
	const HomeButton = document.getElementById('toHome')
	HomeButton.addEventListener('click', eventLoadView);
	HomeButton.desiredView = 'home.view';

	const ProfileButton = document.getElementById('toProfile')
	ProfileButton.addEventListener('click', eventLoadView);
	ProfileButton.desiredView = 'profile.view';
}

function configureNavbarUserDropdown(){
	// const UserDropdown = document.getElementById('dropdownMenuButtonUser');
	// UserDropdown.addEventListener('click', function(){
	// 	$('.dropdown-toggle').dropdown('toggle');
	// });

	const UpdateUserButton = document.getElementById('toUpdateUser');
	UpdateUserButton.addEventListener('click', eventLoadView);
	UpdateUserButton.desiredView = 'user_update.view';

	const RegisterUserButton = document.getElementById('toRegisterUser');
	RegisterUserButton.addEventListener('click', eventLoadView);
	RegisterUserButton.desiredView = 'user_register.view';

	const ViewUserButton = document.getElementById('toViewUsers');
	ViewUserButton.addEventListener('click', eventLoadView);
	ViewUserButton.desiredView = 'user_view.view';

	const DeleteUserButton = document.getElementById('toDeleteUser');
	DeleteUserButton.addEventListener('click', eventLoadView);
	DeleteUserButton.desiredView = 'user_delete.view';
}

function configureNavbarReimbursementDropdown(){
	// const ReimbursementDropdown = document.getElementById('dropdownMenuButtonReimbursement');
	// ReimbursementDropdown.addEventListener('click', function(){
	// 	$('.dropdown-toggle').dropdown('toggle');
	// });

	const CreateReimbursementButton = document.getElementById('toCreateReimbursement');
	CreateReimbursementButton.addEventListener('click', eventLoadView);
	CreateReimbursementButton.desiredView = 'reimbursement_create.view';

	const UpdateReimbursementButton = document.getElementById('toUpdateReimbursement');
	UpdateReimbursementButton.addEventListener('click', eventLoadView);
	UpdateReimbursementButton.desiredView = 'reimbursement_update.view';

	const ViewReimbursementButton = document.getElementById('toViewReimbursement');
	ViewReimbursementButton.addEventListener('click', eventLoadView);
	ViewReimbursementButton.desiredView = 'reimbursement_view.view';

	const DeleteReimbursementButton = document.getElementById('toDeleteReimbursement');
	DeleteReimbursementButton.addEventListener('click', eventLoadView);
	DeleteReimbursementButton.desiredView = 'reimbursement_view.view';
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
			loadView('home.view');

		} else if (xhr.readyState == 4 && xhr.status == 401){

			document.getElementById('login-message').removeAttribute('hidden');
			let err = JSON.parse(xhr.responseText);
			document.getElementById('login-message').innerText = err.message;

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
			loadView("login.view");
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
	let rl = document.getElementById('roleSelectButton').value;

	let credentials = {
		email:em,
		firstName:fn,
		lastName:ln,
		username: un,
		password: pw,
		role: rl
	}
	console.log(credentials);

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
			loadView("user_register.view");

		} else if (xhr.readyState == 4 && xhr.status == 400){

			document.getElementById('reg-message').removeAttribute('hidden');
			let err = JSON.parse(xhr.responseText);
			document.getElementById('reg-message').innerText = err.message;

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
    let rl = document.getElementById('roleSelectButton').value;

    if (!fn || !ln || !email || !un || !pw || !rl) {
        document.getElementById('reg-message').removeAttribute('hidden');
        document.getElementById('reg-message').innerText = 'You must provided values for all fields in the form!'
        document.getElementById('register').setAttribute('disabled', true);
    } else {
        document.getElementById('register').removeAttribute('disabled');
        document.getElementById('reg-message').setAttribute('hidden', true);
    }
}

//#endregion
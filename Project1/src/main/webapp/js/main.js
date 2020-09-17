const APP_VIEW = document.getElementById('app-view');
const NAV_BAR = document.getElementById('navbar');

window.addEventListener('beforeunload', unload);
window.addEventListener('load', init);

function unload(e) {
  // window.localStorage.setItem('unloadTime') = JSON.stringify(new Date());

};

function init() { //function w/out a name = anon function
	// if(window.localStorage.getItem('unloadTime')){
	// 	let loadTime = new Date();
	// 	let unloadTime = new Date(JSON.parse(window.localStorage.getItem('unloadTime')));
	// 	let refreshTime = loadTime.getTime() - unloadTime.getTime();

	// 	if(refreshTime>3000)//3000 milliseconds
	// 	{
	// 		window.localStorage.removeItem("authUser");
	// 		logout();
	// 	}
	// }
	loadView("login.view");
	$('.dropdown-toggle').dropdown();
}

//#region loaders

async function loadView(pageToLoad) {
	console.log('in ' + pageToLoad);
	if (pageToLoad == 'home.view') {
		if (!localStorage.getItem('authUser')) {
			console.log('No user logged in, navigating to login screen');
			loadView("login.view");
			return;
		}
	}
	let xhr = new XMLHttpRequest;
	// xhr.responseType();
	xhr.open("GET", pageToLoad, true); // third parameter of this method is optional (defaults to true)
	xhr.send();
	xhr.onreadystatechange = async function () {
	if (xhr.readyState == 4 && xhr.status == 200) {
		APP_VIEW.innerHTML = xhr.responseText;
		switch (pageToLoad) {
		default:
			loadView('login.view');
			return;
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
			if (!isAuthorized()) return;
			configureRegisterView();
			break;
		case 'user_update.view':
			if (!isAuthorized()) return;
			configureUserUpdateView();
			break;
		case 'user_view.view':
		case 'user_delete.view':
			if (!isAuthorized()) return;
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

function eventLoadView(evt) {
  loadView(evt.currentTarget.desiredView);
}

function loadNavBar() {
  console.log('in loadNavBar');
  let xhr = new XMLHttpRequest;
  let destNav = "main.nav";
  let authUser = JSON.parse(localStorage.getItem('authUser'));
  var authRole = "main";
  if (authUser) {
	console.log(authUser);
	authRole = authUser.role.toLowerCase();
	destNav = authRole + ".nav";
  }
  console.log(destNav);
  xhr.open("GET", destNav, true);
  xhr.send();
  xhr.onreadystatechange = async function () {
	if (xhr.readyState == 4 && xhr.status == 200) {
	  NAV_BAR.innerHTML = xhr.responseText;
	  switch (authRole) {
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

function configureLoginView() {
  console.log('in configureLoginView');

  document.getElementById('login-message').setAttribute('hidden', true);
  document.getElementById('login-button-container').addEventListener('mouseover', validateLoginForm);
  document.getElementById('login').addEventListener('click', login);
}

function configureRegisterView() {
  console.log('in configureRegisterView()');

  document.getElementById('reg-message').setAttribute('hidden', true);

  document.getElementById('reg-username').addEventListener('blur', isUsernameAvailable);
  document.getElementById('email').addEventListener('blur', isEmailAvailable);

  document.getElementById('register').setAttribute('disabled', true);
  document.getElementById('reg-button-container').addEventListener('mouseover', validateRegisterForm);
  document.getElementById('register').addEventListener('click', register);
}

function configureUserUpdateView(){
	console.log('in configureUserUpdateView()');
  
	document.getElementById('reg-message').setAttribute('hidden', true);
  
	document.getElementById('reg-username').addEventListener('blur', isUsernameAvailable);
	document.getElementById('email').addEventListener('blur', isEmailAvailable);
  
	document.getElementById('register').setAttribute('disabled', true);
	document.getElementById('reg-button-container').addEventListener('mouseover', validateUpdateForm);
	document.getElementById('register').addEventListener('click', update);
	populateUserUpdateInputFields();
}

function configureHomeView() {
  console.log('in configureHomeView');

  let authUser = JSON.parse(localStorage.getItem('authUser'));
  document.getElementById('loggedInUsername').innerText = authUser.username;
}

function configureUsersView() {
  console.log('in configureUsersView()');
  populateUserView();
}

function configureDefaultViewButtons() {
  console.log('in configureDefaultViewButtons');
  const HomeButton = document.getElementById('toHome')
  HomeButton.addEventListener('click', eventLoadView);
  HomeButton.desiredView = 'home.view';

  const ProfileButton = document.getElementById('toProfile')
  ProfileButton.addEventListener('click', eventLoadView);
  ProfileButton.desiredView = 'profile.view';
}

function configureNavbarUserDropdown() {
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

function configureNavbarReimbursementDropdown() {
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

function login() {
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

  xhr.onreadystatechange = function () {
	if (xhr.readyState == 4 && xhr.status == 200) {

	  console.log('login successful!');
	  document.getElementById('login-message').setAttribute('hidden', true);
	  localStorage.setItem('authUser', xhr.responseText);
	  loadView('home.view');

	} else if (xhr.readyState == 4 && xhr.status == 401) {

	  document.getElementById('login-message').removeAttribute('hidden');
	  let err = JSON.parse(xhr.responseText);
	  document.getElementById('login-message').innerText = err.message;

	}
  }
}

function logout() {
	console.log('in logout()');
	localStorage.removeItem('authUser');
	loadView("login.view");
	let xhr = new XMLHttpRequest();
	xhr.open('GET', 'auth');
	xhr.send();
	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 204) {
			console.log('logout successful!');
		}
	}
}

function register() {
	console.log('in register()');

	let fn = document.getElementById('fn').value;
	let ln = document.getElementById('ln').value;
	let em = document.getElementById('email').value;
	let un = document.getElementById('reg-username').value;
	let pw = document.getElementById('reg-password').value;
	let rl = document.getElementById('roleSelectButton').value;

	let credentials = {
		email: em,
		firstName: fn,
		lastName: ln,
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

	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 201) {

			let msg = JSON.parse(xhr.responseText);
			console.log(msg);
			// document.getElementById('reg-message').innerText = msg;
			document.getElementById('reg-message').setAttribute('hidden', true);
			loadView("user_register.view");

		} else if (xhr.readyState == 4 && xhr.status == 400) {

			document.getElementById('reg-message').removeAttribute('hidden');
			let err = JSON.parse(xhr.responseText);
			document.getElementById('reg-message').innerText = err.message;

		}
	}
}

function update() {
	console.log('in update()');

	let eid = document.getElementById('id').value;
	let ac = document.getElementById('active').checked == true;
	let fn = document.getElementById('fn').value;
	let ln = document.getElementById('ln').value;
	let em = document.getElementById('email').value;
	let un = document.getElementById('reg-username').value;
	let pw = document.getElementById('reg-password').value;
	let rl = document.getElementById('roleSelectButton').value;

	let userDTO = {
		id: eid,
		active: ac,
		email: em,
		firstName: fn,
		lastName: ln,
		username: un,
		password: pw,
		role: rl
	}
	console.log(userDTO);

	let userDTOJSON = JSON.stringify(userDTO);

	let xhr = new XMLHttpRequest;
	xhr.open('PUT', 'users');
	xhr.setRequestHeader('Content-type', 'application/json');
	xhr.send(userDTOJSON);

	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 200) {

		let msg = JSON.parse(xhr.responseText);
		console.log(msg);
		// document.getElementById('reg-message').innerText = msg;
		document.getElementById('reg-message').setAttribute('hidden', true);
		loadView("user_view.view");

		} else if (xhr.readyState == 4 && xhr.status == 400) {

		document.getElementById('reg-message').removeAttribute('hidden');
		let err = JSON.parse(xhr.responseText);
		document.getElementById('reg-message').innerText = err.message;

		}
	}
}

function populateUserView() {
  console.log('in populateUserView');

  let tableBody = document.getElementById('userTableBody');

  let xhr = new XMLHttpRequest();

  xhr.open('GET', 'users');
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.send();

  xhr.onreadystatechange = function () {
	if (xhr.readyState == 4 && xhr.status == 200) {
		for(let child of tableBody.childNodes){
			child.remove();
		}
		let users = JSON.parse(xhr.responseText);
		for (var i = 0; i < users.length; i++) {
			let row = document.createElement("tr");
			row.classList.add("userEntry");
			let active = users[i].active;
			if (!active) {
				row.classList.add('disabled');
			}
			let id = document.createElement("td");
			id.innerText = users[i].id;
			row.id = users[i].id;
			let firstName = document.createElement("td");
			firstName.innerText = users[i].firstName;
			let lastName = document.createElement("td");
			lastName.innerText = users[i].lastName;
			let username = document.createElement("td");
			username.innerText = users[i].username;
			let email = document.createElement("td");
			email.innerText = users[i].email;
			let role = document.createElement("td");
			role.innerText = titleCase(users[i].role);
			let modify = document.createElement('td');
			modify.id = `modify:${users[i].id}`;

			let pid = users[i].id;
			let ac = users[i].active;
			let em = users[i].email;
			let un = users[i].username;
			let rl = titleCase(users[i].role);

			let principal = {
				"id": pid,
				"username": un,
				"role": rl
			}

			let user = {
				id: pid,
				active: ac,
				username: un,
				email: em,
				role: rl
			}

			let editInteract = document.createElement('a');
			editInteract.href = "#";
			editInteract.className += 'edit';
			editInteract.innerText = "Edit";
			editInteract.addEventListener('click', eventUpdateUser);
			editInteract.user = user;
			modify.appendChild(editInteract);
			let slash = document.createTextNode(' / ');
			modify.appendChild(slash);
			if (active) {
				let deactivateInteract = document.createElement('a');
				deactivateInteract.id = `deactivate:${users[i].id}`;
				deactivateInteract.href = "#";
				deactivateInteract.className += 'remove';
				deactivateInteract.innerText = "Deactivate";
				deactivateInteract.addEventListener('click', eventDeactivateUser);
				deactivateInteract.principal = principal;
				modify.appendChild(deactivateInteract);
			} else {
				let reactivateInteract = document.createElement('a');
				reactivateInteract.id = `reactivate:${users[i].id}`;
				reactivateInteract.href = "#";
				reactivateInteract.className += 'remove';
				reactivateInteract.innerText = "Activate";
				reactivateInteract.addEventListener('click', eventActivateUser);
				reactivateInteract.principal = principal;
				modify.appendChild(reactivateInteract);
			}

			row.appendChild(id);
			row.appendChild(firstName);
			row.appendChild(lastName);
			row.appendChild(username);
			row.appendChild(email);
			row.appendChild(role);
			row.appendChild(modify);

			tableBody.appendChild(row);
			}
			$('#userTable').DataTable();
			$('.dataTables_length').addClass('bs-select');
			console.log(users);
			// tableBody.innerText = JSON.stringify(users);
		}
	}
}

function populateUserUpdateInputFields(){
	console.log('in populateUserUpdateInputFields()');
	
	let user = JSON.parse(localStorage.getItem('updateUser'));
	console.log(user);
	
	let updateViewHeader = document.getElementById('updateViewHeader');
	updateViewHeader.innerText = 'Revabursement Update: Updating ' + user.username;
	let idBox = document.getElementById('id');
	idBox.removeAttribute('disabled');
	idBox.innerText = user.id;
	idBox.value = user.id;
	idBox.disabled = true;
	let roleSelect = document.getElementById('roleSelectButton');
	for (let i = 0; i < roleSelect.options.length; i++) {
		const opt = roleSelect.options[i];
		console.log(user);
		if(opt.value == user.role.toLowerCase()){
			opt.selected = true;
			break;
		}
	}
	localStorage.removeItem('updateUser');
}

// Edit record
function UpdateUser(user) {
	localStorage.setItem('updateUser', JSON.stringify(user));
	loadView('user_update.view');
}

function eventUpdateUser(evt) {
  UpdateUser(evt.currentTarget.user);
}
// Delete a record
function deleteUser(principal) {
  console.log(principal);
  if (!principal) return;
  let xhr = new XMLHttpRequest();
  xhr.open('DELETE', 'users');
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.send(JSON.stringify(principal));

  xhr.onreadystatechange = function () {
	if (xhr.readyState == 4 && xhr.status == 204) {
	  console.log('User removed successfully!');
	  let deleteMe = document.getElementById(principal.id);
	  deleteMe.remove();
	}
  }
}
function eventDeleteUser(evt) {
  deleteUser(evt.currentTarget.principal);
}
// Delete a record
function deactivateUser(principal) {
  console.log(principal);
  if (!principal) return;
  if(isSelf(principal)){
	  console.log(`You cannot deactivate yourself.`);
	  return;
  }
  let xhr = new XMLHttpRequest();
  xhr.open('DELETE', 'users');
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.send(JSON.stringify(principal));

  xhr.onreadystatechange = function () {
	if (xhr.readyState == 4 && xhr.status == 204) {
	  console.log('User removed successfully!');
	  let disabledRow = document.getElementById(principal.id);
	  disabledRow.classList.add('disabled');
	  let deactivateInteract = document.getElementById(`deactivate:${principal.id}`);
	  deactivateInteract.remove();
	  
	  let reactivateInteract = document.createElement('a');
	  reactivateInteract.id = `reactivate:${principal.id}`;
	  reactivateInteract.href = "#";
	  reactivateInteract.className += 'remove';
	  reactivateInteract.innerText = "Activate";
	  reactivateInteract.addEventListener('click', eventActivateUser);
	  reactivateInteract.principal = principal;

	  let modify = document.getElementById(`modify:${principal.id}`);
	  modify.appendChild(reactivateInteract);

	}
  }
}
function eventDeactivateUser(evt) {
  deactivateUser(evt.currentTarget.principal);
}
// Delete a record
function activateUser(principal) {
  console.log(principal);
  if (!principal) return;
  let xhr = new XMLHttpRequest();
  xhr.open('PUT', 'users');
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.setRequestHeader('activate', true);
  xhr.send(JSON.stringify(principal));

  xhr.onreadystatechange = function () {
	if (xhr.readyState == 4 && xhr.status == 204) {
	  console.log('User reactivated successfully!');
	  let deleteMe = document.getElementById(principal.id);
	  deleteMe.classList.remove('disabled');
	  let reactivateInteract = document.getElementById(`reactivate:${principal.id}`);
	  reactivateInteract.remove();

	  let deactivateInteract = document.createElement('a');
	  deactivateInteract.id = `deactivate:${principal.id}`;
	  deactivateInteract.href = "#";
	  deactivateInteract.className += 'remove';
	  deactivateInteract.innerText = "Deactivate";
	  deactivateInteract.addEventListener('click', eventDeactivateUser);
	  deactivateInteract.principal = principal;

	  let modify = document.getElementById(`modify:${principal.id}`);
	  modify.appendChild(deactivateInteract);

	}
	
  }
}
function eventActivateUser(evt) {
  activateUser(evt.currentTarget.principal);
}

function isUsernameAvailable() {

  console.log('in isUsernameAvailable()');

  let username = document.getElementById('reg-username').value;

  let xhr = new XMLHttpRequest();

  xhr.open('POST', 'username.validate');
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.send(JSON.stringify(username));

  xhr.onreadystatechange = function () {
	if (xhr.readyState == 4 && xhr.status == 204) {
	  console.log('Provided username is available!');
	  document.getElementById('reg-message').setAttribute('hidden', true);
	} else if (xhr.readyState == 4 && xhr.status == 409) {
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

  xhr.onreadystatechange = function () {
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


//#region helper functions
function titleCase(string) {
  var sentence = string.toLowerCase().split(" ");
  for (var i = 0; i < sentence.length; i++) {
	sentence[i] = sentence[i][0].toUpperCase() + sentence[i].slice(1);
  }
  return (sentence.join(" "));
}

function isAuthorized(){
	let authUser = JSON.parse(localStorage.getItem('authUser'));
	if (!authUser) return false;
	if (authUser.role.toLowerCase() != 'admin') {
		console.log('This role is not authorized for this view.');
		loadView("login.view");
		return false;
	}
	return true;
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
	document.getElementById('reg-message').innerText = 'You must provide values for all fields in the form!'
	document.getElementById('register').setAttribute('disabled', true);
  } else {
	document.getElementById('register').removeAttribute('disabled');
	document.getElementById('reg-message').setAttribute('hidden', true);
  }
}

function validateUpdateForm() {

  console.log('in validateRegisterForm()');

  let fn = document.getElementById('fn').value;
  let ln = document.getElementById('ln').value;
  let email = document.getElementById('email').value;
  let un = document.getElementById('reg-username').value;
  let pw = document.getElementById('reg-password').value;
  let rl = document.getElementById('roleSelectButton').value;

  if (fn || ln || email || un || pw || rl) {
	document.getElementById('register').removeAttribute('disabled');
	document.getElementById('reg-message').setAttribute('hidden', true);
  } else {
	document.getElementById('reg-message').removeAttribute('hidden');
	document.getElementById('reg-message').innerText = 'You have not changed a single value in the form!'
	document.getElementById('register').setAttribute('disabled', true);
  }
}

function isSelf(principal){
	let authUser = JSON.parse(localStorage.getItem('authUser'));
	return principal.id == authUser.id;
}
//#endregion
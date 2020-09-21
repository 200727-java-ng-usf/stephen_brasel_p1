/**
 * @author Stephen J. Brasel
 * <code>{@link activateUser}</code>
 * @since 1.0
 */
// .___       .__  __  .__       .__  .__               
// |   | ____ |__|/  |_|__|____  |  | |__|_______ ____  
// |   |/    \|  \   __\  \__  \ |  | |  \___   // __ \ 
// |   |   |  \  ||  | |  |/ __ \|  |_|  |/    /\  ___/ 
// |___|___|  /__||__| |__(____  /____/__/_____ \\___  >
//          \/                 \/              \/    \/ 
//#region Initialize

/**
 * Sets variable document.getElementById('app-view') for convenience.
 */
const APP_VIEW = document.getElementById('app-view');
/**
 * Sets variable document.getElementById('navbar') for convenience.
 */
const NAV_BAR = document.getElementById('navbar');

// window.addEventListener('beforeunload', unload);
window.addEventListener('unload', unload);
window.addEventListener('load', init);

/**
 * Called before the page is unloaded. 
 * Used to set a timestamp so that a logout can be called if 
 */
function unload() {
	// let currentDate = JSON.stringify(new Date());
	// console.log(currentDate);
	// window.localStorage.setItem('unloadTime') = currentDate;

};

/**
 * Called when page is initialized. 
 * Loads the view and enables dropdown boxes.
 */
function init() {
	// if(window.localStorage.getItem('unloadTime')){
	// 	let loadTime = new Date();
	// 	console.log(loadTime);
	// 	let unldTime = window.localStorage.getItem('unloadTime');
	// 	console.log(unldTime);
	// 	console.log(JSON.parse(unldTime));
	// 	let unloadTime = new Date(JSON.parse(unldTime));
	// 	console.log(unloadTime);
	// 	let refreshTime = loadTime.getTime() - unloadTime.getTime();
	// 	console.log(refreshTime);

	// 	if(refreshTime > 3)//3000 milliseconds
	// 	{
	// 		window.localStorage.removeItem("authUser");
	// 		logout();
	// 	}
	// }
	loadView("login.view");
	$('.dropdown-toggle').dropdown();
}

//#endregion

// .____                     .___                   
// |    |    _________     __| _/___________  ______
// |    |   /  _ \__  \   / __ |/ __ \_  __ \/  ___/
// |    |__(  <_> ) __ \_/ /_/ \  ___/|  | \/\___ \ 
// |_______ \____(____  /\____ |\___  >__|  /____  >
//         \/         \/      \/    \/           \/ 
//#region loaders

/**
 * Generic function for loading html sections and inserting them into the single-page website. 
 * Takes in a string, '<location>.view', that will load location into the page.
 * @param {string} pageToLoad 
 */
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
					if (!isAuthorized(['admin'])) return;
					configureRegisterView();
					break;
				case 'user_update.view':
					if (!isAuthorized(['admin'])) return;
					configureUserUpdateView();
					break;
				case 'user_view.view':
				case 'user_delete.view':
					if (!isAuthorized(['admin'])) return;
					configureUsersView();
					break;
				case 'reimbursement_create.view':
					configureReimbursementCreateView();
					break;
				case 'reimbursement_update.view':
					configureReimbursementUpdateView();
					break;
				case 'reimbursement_view.view':
					configureReimbursementsView();
					break;
			}
			loadNavBar();
		}
	}
}

/**
 * An event for loading a view. 
 * Directly calls loadView(desiredView).  
 * @param {Event} evt 
 */
function eventLoadView(evt) {
	loadView(evt.currentTarget.desiredView);
}

/**
 * Generic function for loading the navbar of different roles of user. 
 */
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
					LoginButton = document.getElementById('toLogin');
					LoginButton.addEventListener('click', eventLoadView);
					LoginButton.desiredView = 'login.view';
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

// _________                _____.__                            __  .__                      
// \_   ___ \  ____   _____/ ____\__| ____  __ ______________ _/  |_|__| ____   ____   ______
// /    \  \/ /  _ \ /    \   __\|  |/ ___\|  |  \_  __ \__  \\   __\  |/  _ \ /    \ /  ___/
// \     \___(  <_> )   |  \  |  |  / /_/  >  |  /|  | \// __ \|  | |  (  <_> )   |  \\___ \ 
//  \______  /\____/|___|  /__|  |__\___  /|____/ |__|  (____  /__| |__|\____/|___|  /____  >
//         \/            \/        /_____/                   \/                    \/     \/ 
//#region configurations

/**
 * Configures the Login html such that it behaves the way it's supposed to. 
 * Adds events for validation and login.
 */
function configureLoginView() {
	console.log('in configureLoginView');

	document.getElementById('login-message').setAttribute('hidden', true);
	document.getElementById('login-button-container').addEventListener('mouseover', validateLoginForm);
	document.getElementById('login').addEventListener('click', login);
}

/**
 * Configures the Register html such that it behaves the way it's supposed to. 
 * Adds events for validation, registration, and availability.
 */
function configureRegisterView() {
	console.log('in configureRegisterView()');

	document.getElementById('reg-message').setAttribute('hidden', true);

	document.getElementById('reg-username').addEventListener('blur', isUsernameAvailable);
	document.getElementById('email').addEventListener('blur', isEmailAvailable);

	document.getElementById('register').setAttribute('disabled', true);
	document.getElementById('reg-button-container').addEventListener('mouseover', validateRegisterForm);
	document.getElementById('register').addEventListener('click', registerUserRequest);
}

/** 
 * Configures the User Update html such that it behaves the way it's supposed to. 
 * Adds events for validation, registration, and availability.
 */
function configureUserUpdateView() {
	console.log('in configureUserUpdateView()');

	document.getElementById('reg-message').setAttribute('hidden', true);

	document.getElementById('reg-username').addEventListener('blur', isUsernameAvailable);
	document.getElementById('email').addEventListener('blur', isEmailAvailable);

	document.getElementById('register').setAttribute('disabled', true);
	document.getElementById('reg-button-container').addEventListener('mouseover', validateUpdateForm);
	document.getElementById('register').addEventListener('click', updateUserRequest);
	populateUserUpdateInputFields();
}

/**
 * Configures the Home html such that it behaves the way it's supposed to. 
 * Sets the user name to appear on the screen.
 */
function configureHomeView() {
	console.log('in configureHomeView');

	let authUser = JSON.parse(localStorage.getItem('authUser'));
	document.getElementById('loggedInUsername').innerText = authUser.username;
}

/**
 * Configures the Users html such that it behaves the way it's supposed to. 
 * populates the user view with all users.
 */
function configureUsersView() {
	console.log('in configureUsersView()');
	populateUserView();
}

/**
 * Configures the Reimbursement Update html such that it behaves the way it's supposed to. 
 * adds listeners for validation and updating, 
 * populates the update view with the desired reimbursement information.
 */
function configureReimbursementUpdateView() {
	console.log('in configureReimbursementUpdateView();');

	document.getElementById('reg-message').setAttribute('hidden', true);

	document.getElementById('update-reimbursement').setAttribute('disabled', true);
	document.getElementById('sub-button-container').addEventListener('mouseover', validateReimbursementUpdateForm);
	document.getElementById('update-reimbursement').addEventListener('click', updateReimbursementRequest);
	// $('.file-upload').file_upload();
	populateReimbursementsUpdateView();
}

/**
 * Configures the Reimbursement Create html such that it behaves the way it's supposed to. 
 * Adds listeners for validation and submission. 
 */
function configureReimbursementCreateView() {
	console.log('in configureReimbursementCreateView();');

	document.getElementById('reg-message').setAttribute('hidden', true);

	document.getElementById('submit-reimbursement').setAttribute('disabled', true);
	document.getElementById('sub-button-container').addEventListener('mouseover', validateReimbursementCreateForm);
	document.getElementById('submit-reimbursement').addEventListener('click', submitReimbursementRequest);
	// $('.file-upload').file_upload();

}

/**
 * Configures the Reimbursements html such that it behaves the way it's supposed to. 
 * populates the DataTable of reimbursements. 
 */
function configureReimbursementsView() {
	console.log('in configureReimbursementsView()');
	populateReimbursementsView();

}

/**
 * Configures the Default Navbar buttons, Home and Profile, such that they behaves the way they're supposed to. 
 * adds listeners for clicking on the buttons.
 */
function configureDefaultViewButtons() {
	console.log('in configureDefaultViewButtons');
	const HomeButton = document.getElementById('toHome')
	HomeButton.addEventListener('click', eventLoadView);
	HomeButton.desiredView = 'home.view';

	const ProfileButton = document.getElementById('toProfile')
	ProfileButton.addEventListener('click', eventLoadView);
	ProfileButton.desiredView = 'profile.view';
}

/**
 * Configures the Navbar user dropdown html such that it behaves the way it's supposed to. 
 * add listeners for click events.
 */
function configureNavbarUserDropdown() {
	// const UserDropdown = document.getElementById('dropdownMenuButtonUser');
	// UserDropdown.addEventListener('click', function(){
	// 	$('.dropdown-toggle').dropdown('toggle');
	// });

	//   const UpdateUserButton = document.getElementById('toUpdateUser');
	//   UpdateUserButton.addEventListener('click', eventLoadView);
	//   UpdateUserButton.desiredView = 'user_update.view';

	const RegisterUserButton = document.getElementById('toRegisterUser');
	RegisterUserButton.addEventListener('click', eventLoadView);
	RegisterUserButton.desiredView = 'user_register.view';

	const ViewUserButton = document.getElementById('toViewUsers');
	ViewUserButton.addEventListener('click', eventLoadView);
	ViewUserButton.desiredView = 'user_view.view';

	//   const DeleteUserButton = document.getElementById('toDeleteUser');
	//   DeleteUserButton.addEventListener('click', eventLoadView);
	//   DeleteUserButton.desiredView = 'user_delete.view';
}

/**
 * Configures the Navbar Reimbursement html such that it behaves the way it's supposed to. 
 * adds listeners for click events.
 */
function configureNavbarReimbursementDropdown() {
	// const ReimbursementDropdown = document.getElementById('dropdownMenuButtonReimbursement');
	// ReimbursementDropdown.addEventListener('click', function(){
	// 	$('.dropdown-toggle').dropdown('toggle');
	// });

	const CreateReimbursementButton = document.getElementById('toCreateReimbursement');
	CreateReimbursementButton.addEventListener('click', eventLoadView);
	CreateReimbursementButton.desiredView = 'reimbursement_create.view';

	//   const UpdateReimbursementButton = document.getElementById('toUpdateReimbursement');
	//   UpdateReimbursementButton.addEventListener('click', eventLoadView);
	//   UpdateReimbursementButton.desiredView = 'reimbursement_update.view';

	const ViewReimbursementButton = document.getElementById('toViewReimbursement');
	ViewReimbursementButton.addEventListener('click', eventLoadView);
	ViewReimbursementButton.desiredView = 'reimbursement_view.view';

	//   const DeleteReimbursementButton = document.getElementById('toDeleteReimbursement');
	//   DeleteReimbursementButton.addEventListener('click', eventLoadView);
	//   DeleteReimbursementButton.desiredView = 'reimbursement_view.view';
}

//#endregion

// ________                              __  .__                      
// \_____  \ ______   ________________ _/  |_|__| ____   ____   ______
//  /   |   \\____ \_/ __ \_  __ \__  \\   __\  |/  _ \ /    \ /  ___/
// /    |    \  |_> >  ___/|  | \// __ \|  | |  (  <_> )   |  \\___ \ 
// \_______  /   __/ \___  >__|  (____  /__| |__|\____/|___|  /____  >
//         \/|__|        \/           \/                    \/     \/ 
//#region operations

/**
 * bundles the username and password into a credentials object, 
 * sends it to the server.
 * If successful, loads the home view.
 * If unsuccessful, shows error message to the user. 
 */
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

/**
 * loads the login view, 
 * sends message to server to terminate session. 
 */
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

/**
 * bundles the user information into a JSON object, 
 * sends it to the server.
 * If successful, reloads page to register additional users. 
 * If unsuccessful, displays error message to the user.
 */
function registerUserRequest() {
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

/**
 * Bundles reimbursement info into a Data Transfer Object, 
 * sends that to the server.
 * If successful, reloads reimbursement creation page for additional reimbursements. 
 * If unsuccessful, displays error message to the user. 
 */
function submitReimbursementRequest() {
	console.log('in submitReimbursementRequest()');

	let amt = document.getElementById('amount').value;
	let desc = document.getElementById('description').value;
	let rcpt = ''; //document.getElementById('').value; //TODO coming soon
	let author = JSON.parse(localStorage.getItem('authUser')).username;
	let reimb_type = document.getElementById('reimbursementType').value;

	let reimbDto = {
		amount: amt,
		description: desc,
		receiptURI: rcpt,
		author: author,
		type: reimb_type
	}
	console.log(reimbDto);

	let reimbDtoJSON = JSON.stringify(reimbDto);

	let xhr = new XMLHttpRequest;
	xhr.open('POST', 'reimbursements');
	xhr.setRequestHeader('Content-type', 'application/json');
	xhr.send(reimbDtoJSON);

	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 201) {

			let msg = JSON.parse(xhr.responseText);
			console.log(msg);
			// document.getElementById('reg-message').innerText = msg;
			document.getElementById('reg-message').setAttribute('hidden', true);
			loadView("reimbursement_create.view");

		} else if (xhr.readyState == 4 && xhr.status == 400) {

			document.getElementById('reg-message').removeAttribute('hidden');
			let err = JSON.parse(xhr.responseText);
			document.getElementById('reg-message').innerText = err.message;

		}
	}
}

/**
 * Bundles updated user information together and sends it to the server.
 * If successful, loads the users DataTable view. 
 * If unsuccessful, displays error message to the user.
 */
function updateUserRequest() {
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

/**
 * Bundles updated reimbursement information together and sends it to the server.
 * If successful, loads reimbursement DataTable view. 
 * If unsuccessful, displays error message to the user. 
 */
function updateReimbursementRequest() {
	console.log('in updateReimbursementRequest()');

	let rid = document.getElementById('id').value;
	let amt = document.getElementById('amount').value;
	let sub = new Date(document.getElementById('submitted').value);
	console.log(sub);
	sub = formatTime(sub);
	console.log(sub);
	let res = new Date(document.getElementById('resolved').value);
	console.log(res);
	if (isNaN(res.getTime())) {
		res = null;
	} else {
		res = formatTime(res);
	}
	console.log(res);
	let des = document.getElementById('description').value;
	let rec = document.getElementById('receipts').value;
	let aut = document.getElementById('author').value;
	let olv = document.getElementById('resolver').value;
	let sta = document.getElementById('reimbursementStatus').value;
	let typ = document.getElementById('reimbursementType').value;

	let reimbursementDTO = {
		id: rid,
		amount: amt,
		submitted: sub,
		resolved: res,
		description: des,
		receiptURI: rec,
		author: aut,
		resolver: olv,
		reimb_status_id: sta,
		reimb_type_id: typ
	}
	console.log(reimbursementDTO);

	let reimbursementDTOJSON = JSON.stringify(reimbursementDTO);

	let xhr = new XMLHttpRequest;
	xhr.open('PUT', 'reimbursements');
	xhr.setRequestHeader('Content-type', 'application/json');
	xhr.send(reimbursementDTOJSON);

	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 200) {

			let msg = JSON.parse(xhr.responseText);
			console.log(msg);
			// document.getElementById('reg-message').innerText = msg;
			document.getElementById('reg-message').setAttribute('hidden', true);
			loadView("reimbursement_view.view");

		} else if (xhr.readyState == 4 && xhr.status == 400) {

			document.getElementById('reg-message').removeAttribute('hidden');
			let err = JSON.parse(xhr.responseText);
			document.getElementById('reg-message').innerText = err.message;

		}
	}

}

/**
 * Requests Users from the server.
 * Upon Success, parses the information into the Users DataTable. 
 */
function populateUserView() {
	console.log('in populateUserView');

	let tableBody = document.getElementById('userTableBody');

	let xhr = new XMLHttpRequest();

	xhr.open('GET', 'users');
	xhr.setRequestHeader('Content-type', 'application/json');
	xhr.send();

	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 200) {
			for (let child of tableBody.childNodes) {
				child.remove();
			}
			let users = JSON.parse(xhr.responseText);
			for (var i = 0; i < users.length; i++) {
				let row = document.createElement("tr");
				row.classList.add("userEntry");
				let active = users[i].active;
				if (!active) {
					row.classList.add('deactivated');
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
				let editIcon = document.createElement('span');
				editIcon.classList += "glyphicon glyphicon-wrench";
				editInteract.appendChild(editIcon);
				editInteract.innerText = "Edit";
				// editInteract.setAttribute("data-toggle", ["tooltip"]);
				editInteract.title = "Update User";
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
					let deactivateIcon = document.createElement('span');
					deactivateIcon.classList += "glyphicon glyphicon-remove-circle";
					deactivateInteract.appendChild(deactivateIcon);
					deactivateInteract.innerText = "Deactivate";
					// deactivateInteract.setAttribute("data-toggle", ["tooltip"]);
					deactivateInteract.title = "Deactivate User";
					deactivateInteract.addEventListener('click', eventDeactivateUser);
					deactivateInteract.principal = principal;
					modify.appendChild(deactivateInteract);
				} else {
					let reactivateInteract = document.createElement('a');
					reactivateInteract.id = `reactivate:${users[i].id}`;
					reactivateInteract.href = "#";
					reactivateInteract.className += 'ok';
					let reactivateIcon = document.createElement('span');
					reactivateIcon.classList += "glyphicon glyphicon-ok-circle";
					reactivateInteract.appendChild(reactivateIcon);
					reactivateInteract.innerText = "Activate";
					// reactivateInteract.setAttribute("data-toggle", ["tooltip"]);
					reactivateInteract.title = "Reactivate User";
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
			$('[data-toggle="tooltip"]').tooltip()
			console.log(users);
			// tableBody.innerText = JSON.stringify(users);
		}
	}
}

/**
 * Requests Reimbursements from the server, if Employee only requests personal reimbursements. 
 * Upon success, parses the information into the Reimbursement DataTable.  
 */
function populateReimbursementsView() {

	console.log('in populateReimbursementsView');

	let tableBody = document.getElementById('reimbursementTableBody');

	let xhr = new XMLHttpRequest();
	let authUser = JSON.parse(localStorage.getItem('authUser'));
	if (isAuthorized(['admin', 'manager'])) {
		xhr.open('GET', 'reimbursements');
	} else {
		xhr.open('GET', `reimbursements?author=${authUser.id}`);
	}
	xhr.setRequestHeader('Content-type', 'application/json');
	xhr.send();

	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 200) {
			//remove previous table states, if any.
			for (let child of tableBody.childNodes) {
				child.remove();
			}
			let reimbursements = JSON.parse(xhr.responseText);
			for (var i = 0; i < reimbursements.length; i++) {
				let row = document.createElement("tr");
				row.classList.add("reimbursementEntry");

				let rid = reimbursements[i].id;
				let amt = reimbursements[i].amount;
				let submitd = reimbursements[i].submitted;
				let submtd = reimbursements[i].submitted;
				if (submtd) submtd = (new Date(submtd)).toString();
				let reslvd = reimbursements[i].resolved;
				let rslvd = reimbursements[i].resolved;
				if (rslvd) rslvd = (new Date(rslvd)).toString();
				let desc = reimbursements[i].description;
				let rcptURI = reimbursements[i].receiptURI;
				let authr = reimbursements[i].author;
				if (authr) authr = authr.username;
				let rslvr = reimbursements[i].resolver;
				if (rslvr) rslvr = rslvr.username;
				let reimb_status = reimbursements[i].reimb_status_id;
				if (reimb_status) reimb_status = titleCase(reimb_status);
				let reimb_type = reimbursements[i].reimb_type_id;
				if (reimb_type) reimb_type = titleCase(reimb_type);

				let idElem = document.createElement("td");
				idElem.innerText = rid;
				row.id = rid;
				let amountElem = document.createElement("td");
				amountElem.innerText = amt;
				let submittedElem = document.createElement("td");
				submittedElem.innerText = submtd;
				let resolvedElem = document.createElement("td");
				resolvedElem.innerText = rslvd;
				let descriptionElem = document.createElement("td");
				descriptionElem.innerText = desc;
				let receiptURIElem = document.createElement("td");
				receiptURIElem.innerText = rcptURI;
				let authorElem = document.createElement("td");
				authorElem.innerText = authr;
				let resolverElem = document.createElement("td");
				resolverElem.innerText = rslvr;
				let reimbursementStatusElem = document.createElement("td");
				reimbursementStatusElem.innerText = reimb_status;
				let reimbursementTypeElem = document.createElement("td");
				reimbursementTypeElem.innerText = reimb_type;
				let modifyElem = document.createElement('td');
				modifyElem.id = `modify:${rid}`;

				let reimbursement = {
					id: rid,
					amount: amt,
					submitted: submitd,
					resolved: reslvd,
					description: desc,
					receiptURI: rcptURI,
					author: authr,
					resolver: rslvr,
					reimbursementStatus: reimb_status,
					reimbursementType: reimb_type
				}

				let deleteId = {
					id: rid
				}

				if (authUser.role != "employee" || reimb_status.toLowerCase() == "pending") {

					let editInteract = document.createElement('a');
					editInteract.href = "#";
					editInteract.className += 'edit';
					let editIcon = document.createElement('span');
					editIcon.classList += "glyphicon glyphicon-wrench";
					editInteract.appendChild(editIcon);
					editInteract.innerText = "Edit";
					// editInteract.innerHTML = '<span class="glyphicon glyphicon-wrench"></span>';
					// editInteract.setAttribute("data-toggle", ["tooltip"]);
					editInteract.title = "Update Reimbursement";
					editInteract.addEventListener('click', eventUpdateReimbursement);
					editInteract.reimbursement = reimbursement;
					modifyElem.appendChild(editInteract);

				}
				if (authUser.role != "employee") {
					let slash = document.createTextNode(' / ');
					modifyElem.appendChild(slash);

					let deleteInteract = document.createElement('a');
					deleteInteract.id = `deactivate:${reimbursements[i].id}`;
					deleteInteract.href = "#";
					deleteInteract.className += 'remove';
					let deleteIcon = document.createElement('span');
					deleteIcon.classList += "glyphicon glyphicon-remove-circle";
					deleteInteract.appendChild(deleteIcon);
					deleteInteract.innerText = "Delete";
					// deleteInteract.innerHTML = '<span class="glyphicon glyphicon-remove-circle"></span>';
					// deleteInteract.setAttribute("data-toggle", ["tooltip"]);
					deleteInteract.title = "Delete Reimbursement";
					deleteInteract.addEventListener('click', eventDeleteReimbursement);
					deleteInteract.id = deleteId;
					modifyElem.appendChild(deleteInteract);
				}

				row.classList.add(reimb_status.toLowerCase());

				row.appendChild(idElem);
				row.appendChild(amountElem);
				row.appendChild(submittedElem);
				row.appendChild(resolvedElem);
				row.appendChild(descriptionElem);
				row.appendChild(receiptURIElem);
				row.appendChild(authorElem);
				row.appendChild(resolverElem);
				row.appendChild(reimbursementStatusElem);
				row.appendChild(reimbursementTypeElem);
				row.appendChild(modifyElem);

				tableBody.appendChild(row);
			}
			$('#reimbursementTable').DataTable();
			$('.dataTables_length').addClass('bs-select');
			$('[data-toggle="tooltip"]').tooltip()
			console.log(reimbursements);
			// tableBody.innerText = JSON.stringify(reimbursements);
		}
	}
}

/**
 * Grabs locally stored user information,
 * and populates the html elements with the parsed data. 
 */
function populateUserUpdateInputFields() {
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
		if (opt.value == user.role.toLowerCase()) {
			opt.selected = true;
			break;
		}
	}
	localStorage.removeItem('updateUser');
}

/**
 * Grabs locally stored reimbursement and populates the html elements with the parsed data. 
 * If user role is Employee, they will be unable to change the reimbursement if it is no longer pending. 
 * 
 */
function populateReimbursementsUpdateView() {
	let numSlice = 23;
	console.log('in populateReimbursementsUpdateView()');
	//if user is Employee AND reimbursementStatus is NOT pending
	let authUser = JSON.parse(localStorage.getItem('authUser'));
	let reimbursement = JSON.parse(localStorage.getItem('updateReimbursement'));
	console.log(authUser);
	console.log(reimbursement);
	if (authUser) {
		if (authUser.role.toLowerCase() == "employee"
			&& reimbursement.reimbursementStatus.toLowerCase() != "pending") {
			document.getElementById('sub-button-container').removeEventListener('mouseover', validateReimbursementUpdateForm);
			document.getElementById('update-reimbursement').removeEventListener('click', updateReimbursementRequest);
		}
	}


	let updateViewHeader = document.getElementById('updateViewHeader');
	updateViewHeader.innerText = 'Revabursement Update: Updating Reimbursement for ' + reimbursement.author;

	editDisabledValue('id', reimbursement.id);
	editDisabledValue('submitted', formatTime(new Date(reimbursement.submitted)).slice(0, numSlice).replace(' ', 'T'));
	editDisabledValue('author', reimbursement.author);
	editDisabledValue('resolver', reimbursement.resolver);
	selectChosenOption('reimbursementStatus', reimbursement.reimbursementStatus);
	selectChosenOption('reimbursementType', reimbursement.reimbursementType);

	let amount = document.getElementById('amount');
	amount.value = reimbursement.amount;
	if (reimbursement.resolved) {
		editDisabledValue('resolved', formatTime(new Date(reimbursement.resolved)).slice(0, numSlice).replace(' ', 'T'));
	}
	let description = document.getElementById('description');
	description.value = reimbursement.description;
	let receiptURI = document.getElementById('receiptURI');
	receiptURI.value = reimbursement.receiptURI;

	localStorage.removeItem('updateReimbursement');
	//if user is Employee AND reimbursementStatus is NOT pending
	authUser = JSON.parse(localStorage.getItem('authUser'));
	if (authUser) {
		if (authUser.role.toLowerCase() == "employee") {
			if (reimbursement.reimbursementStatus.toLowerCase() != "pending") {
				//disable all controls. 

				document.getElementById('id').disabled = true;
				document.getElementById('amount').disabled = true;
				document.getElementById('submitted').disabled = true;
				document.getElementById('resolved').disabled = true;
				document.getElementById('description').disabled = true;
				document.getElementById('receipts').disabled = true;
				document.getElementById('receiptURI').disabled = true;
				document.getElementById('author').disabled = true;
				document.getElementById('resolver').disabled = true;
				document.getElementById('reimbursementType').disabled = true;
				document.getElementById('sub-button-container').removeEventListener('mouseover', validateReimbursementUpdateForm);
				// document.getElementById('sub-button-container').hidden = true; 
				document.getElementById('update-reimbursement').removeEventListener('click', updateReimbursementRequest);
				// document.getElementById('update-reimbursement').hidden = true;
			}
			document.getElementById('reimbursementStatus').disabled = true;
		}
	}
}

/**
 * A helper function for adding the selected class to the appropriately selected element. 
 * @param {integer} id 
 * @param {string} option 
 */
function selectChosenOption(id, option) {
	let selection = document.getElementById(id);
	for (let i = 0; i < selection.options.length; i++) {
		const opt = selection.options[i];
		if (opt.value == option.toLowerCase()) {
			opt.selected = true;
			break;
		}
	}

}
/**
 * Edit a record and update the information by loading the update view. 
 * @param {User} user 
 */
function UpdateUser(user) {
	localStorage.setItem('updateUser', JSON.stringify(user));
	loadView('user_update.view');
}

/**
 * An event to load the user update view, 
 * called for UpdateUser(user). 
 * @param {Event} evt 
 */
function eventUpdateUser(evt) {
	UpdateUser(evt.currentTarget.user);
}

/**
 * Requests to delete a user from the database. 
 * If successful, DataTable information is omitted. 
 * @param {Principal} principal 
 */
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

/**
 * An event to call deleteUser(principal). 
 * @param {Event} evt 
 */
function eventDeleteUser(evt) {
	deleteUser(evt.currentTarget.principal);
}

/**
 * Sends a request to the server to deactivate a certain user with information 'principal'. 
 * If successful, modifies DataTable to appropriately reflect deactivated status.
 * @param {Principal} principal 
 */
function deactivateUser(principal) {
	console.log(principal);
	if (!principal) return;
	if (isSelf(principal)) {
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
			disabledRow.classList.add('deactivated');
			let deactivateInteract = document.getElementById(`deactivate:${principal.id}`);
			deactivateInteract.remove();

			let reactivateInteract = document.createElement('a');
			reactivateInteract.id = `reactivate:${principal.id}`;
			reactivateInteract.href = "#";
			reactivateInteract.className += 'ok';
			let reactivateIcon = document.createElement('span');
			reactivateIcon.classList += "glyphicon glyphicon-ok-circle";
			reactivateInteract.appendChild(reactivateIcon);
			reactivateInteract.innerText = "Activate";
			//   reactivateInteract.setAttribute("data-toggle", ["tooltip"]);
			reactivateInteract.title = "Reactivate User";
			reactivateInteract.addEventListener('click', eventActivateUser);
			reactivateInteract.principal = principal;

			let modify = document.getElementById(`modify:${principal.id}`);
			modify.appendChild(reactivateInteract);

		}
	}
}

/**
 * An event to call deactivateUser(principal)
 * @param {Event} evt 
 */
function eventDeactivateUser(evt) {
	deactivateUser(evt.currentTarget.principal);
}
/**
 * Sends a request to the server to re-activate a deactivated User (all users start activated)
 * Uses the information in principal to activate the correct user. 
 * @param {Principal} principal 
 */
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
			deleteMe.classList.remove('deactivated');
			let reactivateInteract = document.getElementById(`reactivate:${principal.id}`);
			reactivateInteract.remove();

			let deactivateInteract = document.createElement('a');
			deactivateInteract.id = `deactivate:${principal.id}`;
			deactivateInteract.href = "#";
			deactivateInteract.className += 'remove';
			let deactivateIcon = document.createElement('span');
			deactivateIcon.classList += "glyphicon glyphicon-remove-circle";
			deactivateInteract.appendChild(deactivateIcon);
			deactivateInteract.innerText = "Deactivate";
			//   deactivateInteract.setAttribute("data-toggle", ["tooltip"]);
			deactivateInteract.title = "Deactivate User";
			deactivateInteract.addEventListener('click', eventDeactivateUser);
			deactivateInteract.principal = principal;

			let modify = document.getElementById(`modify:${principal.id}`);
			modify.appendChild(deactivateInteract);

		}

	}
}

/**
 * An event to activateUser(principal). 
 * @param {Event} evt 
 */
function eventActivateUser(evt) {
	activateUser(evt.currentTarget.principal);
}

/**
 * Stores the reimbursement to update in local storage, then loads the reimbursement update html.  
 * @param {Reimbursement} reimbursement 
 */
function updateReimbursement(reimbursement) {
	localStorage.setItem('updateReimbursement', JSON.stringify(reimbursement));
	loadView('reimbursement_update.view');
}

/**
 * An event ot updateReimbursement(reimbursement).
 * @param {Event} evt 
 */
function eventUpdateReimbursement(evt) {
	console.log('in eventUpdateReimbursement()');
	updateReimbursement(evt.currentTarget.reimbursement);

}

/**
 * Sends a request to the server to delete a reimbursement with the given id. 
 * If successful, corrects the DataTable to reflect that the reimbursement no longer exists. 	
 * @param {integer} id 
 */
function deleteReimbursement(id) {
	id = JSON.stringify(id);
	console.log(id);
	if (!id) return;
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', 'reimbursements');
	xhr.setRequestHeader('Content-type', 'application/json');
	xhr.send(id);

	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 204) {
			console.log('User removed successfully!');
			let deleteMe = document.getElementById(id.id);
			deleteMe.remove();
		}
	}
}

/**
 * An event to call deleteReimbursement(id). 
 * @param {Event} evt 
 */
function eventDeleteReimbursement(evt) {
	console.log('in eventDeleteReimbursement()');
	deleteReimbursement(evt.currentTarget.id);
}

//#endregion

// ___ ___         .__                        ___________                   __  .__                      
// /   |   \   ____ |  | ______   ___________  \_   _____/_ __  ____   _____/  |_|__| ____   ____   ______
// /    ~    \_/ __ \|  | \____ \_/ __ \_  __ \  |    __)|  |  \/    \_/ ___\   __\  |/  _ \ /    \ /  ___/
// \    Y    /\  ___/|  |_|  |_> >  ___/|  | \/  |     \ |  |  /   |  \  \___|  | |  (  <_> )   |  \\___ \ 
// \___|_  /  \___  >____/   __/ \___  >__|     \___  / |____/|___|  /\___  >__| |__|\____/|___|  /____  >
// 	  \/       \/     |__|        \/             \/             \/     \/                    \/     \/ 
//#region helper functions

/**
 * transforms the given string into a 'Title Case String.'
 * Every Word Is Capitalized.  
 * @param {String} string 
 */
function titleCase(string) {
	var sentence = string.toLowerCase().split(" ");
	for (var i = 0; i < sentence.length; i++) {
		sentence[i] = sentence[i][0].toUpperCase() + sentence[i].slice(1);
	}
	return (sentence.join(" "));
}

/**
 * Checks the current user role against every role in listOfRoles, 
 * if the user is in the list, they are authorized and this function returns true. 
 * If there is no user, returns false. 
 * @param {string[]} listOfRoles 
 */
function isAuthorized(listOfRoles) {
	let authUser = JSON.parse(localStorage.getItem('authUser'));
	if (!authUser) {
		return false;
	}
	let authorized = false;
	for (var i = 0; i < listOfRoles.length; i++) {
		if (authUser.role.toLowerCase() == listOfRoles[i].toLowerCase()) {
			authorized = true;
		}
	}
	return authorized;
}

/**
 * enables an element with the provided id,
 * sets the elements value with the newVal, 
 * re-disables the given element.
 * @param {int} id 
 * @param {any} newVal 
 */
function editDisabledValue(id, newVal) {
	let element = document.getElementById(id);
	element.removeAttribute('disabled');
	element.value = newVal;
	element.disabled = true;
}

/**
 * returns a Universal Time Code Date in format yyyy-MM-dd HH:mm:ss.sss
 * @param {Date} timeF 
 */
function formatUTCTime(timeF) {
	return timeF.getUTCFullYear() + '-' +
		('00' + (timeF.getUTCMonth() + 1)).slice(-2) + '-' +
		('00' + timeF.getUTCDate()).slice(-2) + ' ' +
		('00' + timeF.getUTCHours()).slice(-2) + ':' +
		('00' + timeF.getUTCMinutes()).slice(-2) + ':' +
		('00' + timeF.getUTCSeconds()).slice(-2) + '.' +
		('00' + timeF.getUTCMilliseconds()).slice(-4);
}

/**
 * returns a Date in format yyyy-MM-dd HH:mm:ss.sss
 * @param {Date} timeF 
 */
function formatTime(timeF) {
	return timeF.getFullYear() + '-' +
		('00' + (timeF.getMonth() + 1)).slice(-2) + '-' +
		('00' + timeF.getDate()).slice(-2) + ' ' +
		('00' + timeF.getHours()).slice(-2) + ':' +
		('00' + timeF.getMinutes()).slice(-2) + ':' +
		('00' + timeF.getSeconds()).slice(-2) + '.' +
		('00' + timeF.getMilliseconds()).slice(-4);
}
//#endregion

// ___________                    ____   ____      .__  .__    .___       __  .__               
// \_   _____/__________  _____   \   \ /   /____  |  | |__| __| _/____ _/  |_|__| ____   ____  
//  |    __)/  _ \_  __ \/     \   \   Y   /\__  \ |  | |  |/ __ |\__  \\   __\  |/  _ \ /    \ 
//  |     \(  <_> )  | \/  Y Y  \   \     /  / __ \|  |_|  / /_/ | / __ \|  | |  (  <_> )   |  \
//  \___  / \____/|__|  |__|_|  /    \___/  (____  /____/__\____ |(____  /__| |__|\____/|___|  /
//      \/                    \/                 \/             \/     \/                    \/ 
//#region formValidation

/**
 * sends a request to the server to determine if a given username is in the database. 
 * returns true if there is no matching username in the database. 
 */
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

/**
 * sends a request to the server to determine if a given email is in the database. 
 * returns true if there is no matching email in the database. 
 */
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

/**
 * Checks the Login Form for
 * a username
 * a password
 * returns true if both exist with more than 0 characters. 
 */
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

/**
 * Checks the registration form for 
 * firstname
 * lastname
 * username
 * email
 * password
 * role
 * returns true if all exist with a minimum of 1 character for the string values. 
 */
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

/**
 * Checks the user update form for 
 * firstname
 * lastname
 * username
 * email
 * password
 * role
 * returns true if all exist with a minimum of 1 character for the string values. 
 */
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

/**
 * Checks the reimbursement form for 
 * amount
 * description
 * returns true if amount is a positive number and description has at least one character. 
 */
function validateReimbursementCreateForm() {
	console.log('in validateReimbursementForm()');

	let amnt = document.getElementById('amount').value;
	let desc = document.getElementById('description').value;
	let recpt = document.getElementById('receipts').value;

	if (amnt > 0 && desc) {
		document.getElementById('submit-reimbursement').removeAttribute('disabled');
		document.getElementById('reg-message').setAttribute('hidden', true);
	} else {
		document.getElementById('reg-message').removeAttribute('hidden');
		document.getElementById('reg-message').innerText = 'You need an amount and a description!';
		document.getElementById('submit-reimbursement').setAttribute('disabled', true);
	}
}

/**
 * Checks the reimbursement update form for 
 * amount
 * description
 * returns true if amount is a positive number and description has at least one character. 
 */
function validateReimbursementUpdateForm() {
	console.log('in validateReimbursementUpdateForm()');

	let amnt = document.getElementById('amount').value;
	let desc = document.getElementById('description').value;
	let recpt = document.getElementById('receipts').value;

	if (amnt && desc) {
		document.getElementById('update-reimbursement').removeAttribute('disabled');
		document.getElementById('reg-message').setAttribute('hidden', true);
	} else {
		document.getElementById('reg-message').removeAttribute('hidden');
		document.getElementById('reg-message').innerText = 'You need an amount and a description!';
		document.getElementById('update-reimbursement').setAttribute('disabled', true);
	}
}

/**
 * returns true if the current user id is equal to the principal user's id
 * @param {Principal} principal 
 */
function isSelf(principal) {
	let authUser = JSON.parse(localStorage.getItem('authUser'));
	return principal.id == authUser.id;
}
//#endregion
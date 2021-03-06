<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- set path to resources folder -->
<spring:url value="/resources/" var="resources" />
<html>
<body>
	<script type="text/javascript">
		var successCaption = "<spring:message code="success.caption"/>";
		var contestUserListSuccess = "<spring:message code="listContests.contestInfo.success.save"/>";
		var errorCaption = "<spring:message code="error.caption"/>";
		var contestAccessDenied = "<spring:message code="contest.accessDenied" />";
		var emptyUserListHeader = "<spring:message code="contest.userList.EmptyListHeader"/>";
		var emptyUserListBody = "<spring:message code="contest.userList.EmptyListBody"/>";
		var btnSubmit = "<spring:message code="btn.submit"/>";
		var btnClose = "<spring:message code="btn.close"/>";
		var GOING = "<spring:message code="listContests.status.going" />";
		var PREPARING = "<spring:message code="listContests.status.preparing" />";
		var RELEASE = "<spring:message code="listContests.status.release" />";
		var ARCHIVE = "<spring:message code="listContests.status.archive" />";
	</script>

	<input type="text" id="id" value="0" hidden="true" />

	<div class="row">
		<c:url var="dataUrl" value="/getListOfContests.do" />
		<table id="contestsData" data-toggle="table" data-striped="true"
			data-url="${dataUrl}" data-method="get" data-cache="false"
			data-sort-name="title" data-sort-order="asc" data-pagination="true"
			data-show-pagination-switch="true" data-search="true"
			data-clear-search="true" data-show-columns="true"
			data-minimum-count-columns="0">
			<thead>
				<tr>
					<th data-field="id" data-align="center" data-sortable="true"
						data-switchable="false" data-visible = "false"><spring:message
							code="listContests.contests.tableHeader.id" /></th>
					<th data-field="title" data-align="center" data-sortable="true"
						data-switchable="false"><spring:message
							code="listContests.contests.tableHeader.title" /></th>
					<th data-field="users" data-align="center"
						data-formatter="usersFormatter" data-sortable="true"
						data-sorter="usersSorter"><spring:message
							code="listContests.contests.tableHeader.users" /></th>
					<th data-field="status" data-align="center"
						data-formatter="statusFormatter" data-sortable="true"><spring:message
							code="listContests.contests.tableHeader.status" /></th>
					<th data-field="starts" data-align="center" data-sortable="true"
						data-sorter="dateSorter" data-formatter="dateFormatter"><spring:message
							code="listContests.contests.tableHeader.starts" /></th>
				</tr>
			</thead>
		</table>
	</div>

	<!-- modal -->
	<div id="userListModal" class="modal fade" tabindex="-1">
		<div class="modal-dialog" style="height: 50%">
			<div class="modal-content">
				<div class="modal-header modal-header-info">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4>
						<spring:message code="listContests.contestInfo.header" />
					</h4>
				</div>
				<div class="modal-body">
					<h3 class="text-center">
						<spring:message
							code="listContests.contestInfo.contestStatus.label" />
					</h3>

					<div class="groupRadioStatus">
						<input type="radio" class="radio-input" name="radioStatus"
							id="rbPreparing" value="PREPARING" /> <label class="radio-label"
							for="rbPreparing"><spring:message code="listContests.status.preparing" /></label> 
						<input type="radio"	class="radio-input" name="radioStatus" id="rbRelease"
							value="RELEASE" /> <label class="radio-label" for="rbRelease"><spring:message
								code="listContests.status.release" /></label> 
						<input type="radio"	class="radio-input" name="radioStatus" id="rbGoing" 
						value="GOING" /> <label class="radio-label" for="rbGoing"><spring:message
								code="listContests.status.going" /></label> 
						<input type="radio"	class="radio-input" name="radioStatus" id="rbArchive"
							value="ARCHIVE" /> <label class="radio-label" for="rbArchive"><spring:message
								code="listContests.status.archive" /></label>
					</div>
					<h3 class="text-center">
						<spring:message code="listContests.contestInfo.userList.label" />
					</h3>
					<table id="ContestUserTable" data-toggle="table" data-url=""
						data-classes="table table-hover" data-click-to-select="true">
						<thead>
							<tr>
								<th data-field="chose" data-checkbox="true"></th>
								<th data-field="id" data-sortable="true" class="idField">id</th>
								<th data-field="displayName" data-sortable="true"><spring:message code="contest.userList.displayName" /></th>
								<th data-field="department" data-sortable="true"><spring:message code="contest.userList.department" /></th>
							</tr>
						</thead>
					</table>
				</div>
				<div class="modal-footer">
					<div class="checkboxes">
						<label for="chbHidden"><input type="checkbox"
							id="chbHidden" value="" /> <span><spring:message
									code="listContests.status.hidden" /></span> </label>
					</div>
					<button id="submitUserListButton" type="button"
						class="btn btn-primary">
						<spring:message code="btn.save" />
					</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">
						<spring:message code="btn.close" />
					</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->

</body>
</html>

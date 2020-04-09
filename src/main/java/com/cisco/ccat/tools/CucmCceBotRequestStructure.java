package com.cisco.ccat.tools;

	public class CucmCceBotRequestStructure {
		
		private String id;
		private String name;
		private String targetUrl;
		private String resource;
		private String event;
		private String orgId;
		private String createdBy;
		private String appId;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getTargetUrl() {
			return targetUrl;
		}
		public void setTargetUrl(String targetUrl) {
			this.targetUrl = targetUrl;
		}
		public String getResource() {
			return resource;
		}
		public void setResource(String resource) {
			this.resource = resource;
		}
		public String getEvent() {
			return event;
		}
		public void setEvent(String event) {
			this.event = event;
		}
		public String getOrgId() {
			return orgId;
		}
		public void setOrgId(String orgId) {
			this.orgId = orgId;
		}
		public String getCreatedBy() {
			return createdBy;
		}
		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}
		public String getAppId() {
			return appId;
		}
		public void setAppId(String appId) {
			this.appId = appId;
		}
		public String getOwnedBy() {
			return ownedBy;
		}
		public void setOwnedBy(String ownedBy) {
			this.ownedBy = ownedBy;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getCreated() {
			return created;
		}
		public void setCreated(String created) {
			this.created = created;
		}
		public String getActorId() {
			return actorId;
		}
		public void setActorId(String actorId) {
			this.actorId = actorId;
		}
		public CucmCceBotRequestStructureData getData() {
			return data;
		}
		public void setData(CucmCceBotRequestStructureData data) {
			this.data = data;
		}
		private String ownedBy;
		private String status;
		private String created;
		private String actorId;
		private CucmCceBotRequestStructureData data;

	}

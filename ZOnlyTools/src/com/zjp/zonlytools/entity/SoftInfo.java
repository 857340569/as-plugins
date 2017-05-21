package com.zjp.zonlytools.entity;

import java.util.List;

public class SoftInfo {

	private String SoftName;
	private String Version;
	private String Content;
	private String DownLoadUrl;
	private String vmid;
	private String fmid;
	private String ForceUpdate;
	private String SoftStartImage;
	private String LogCmd;
	private List<DocumentCofig> DocumentCofig;

	public void setDocumentCofig(List<DocumentCofig> DocumentCofig) {
		this.DocumentCofig = DocumentCofig;
	}

	public List<DocumentCofig> getDocumentCofig() {
		return DocumentCofig;
	}

	public void setSoftName(String SoftName) {
		this.SoftName = SoftName;
	}

	public String getSoftName() {
		return SoftName;
	}

	public void setVersion(String Version) {
		this.Version = Version;
	}

	public String getVersion() {
		return Version;
	}

	public void setContent(String Content) {
		this.Content = Content;
	}

	public String getContent() {
		return Content;
	}

	public void setDownLoadUrl(String DownLoadUrl) {
		this.DownLoadUrl = DownLoadUrl;
	}

	public String getDownLoadUrl() {
		return DownLoadUrl;
	}

	public void setVmid(String vmid) {
		this.vmid = vmid;
	}

	public String getVmid() {
		return vmid;
	}

	public void setFmid(String fmid) {
		this.fmid = fmid;
	}

	public String getFmid() {
		return fmid;
	}

	public void setForceUpdate(String ForceUpdate) {
		this.ForceUpdate = ForceUpdate;
	}

	public String getForceUpdate() {
		return ForceUpdate;
	}

	public void setSoftStartImage(String SoftStartImage) {
		this.SoftStartImage = SoftStartImage;
	}

	public String getSoftStartImage() {
		return SoftStartImage;
	}

	public void setLogCmd(String LogCmd) {
		this.LogCmd = LogCmd;
	}

	public String getLogCmd() {
		return LogCmd;
	}

	/**
	 * 登录页面中的网页入口配置信息
	 * @author zjp
	 * @date 2017年5月18日
	 */
	public class DocumentCofig {
		private String Id;
		private String Title;
		private String UserType;
		private String URL;
		private String IsShow;//是否显示

		public void setId(String Id) {
			this.Id = Id;
		}

		public String getId() {
			return Id;
		}

		public void setTitle(String Title) {
			this.Title = Title;
		}

		public String getTitle() {
			return Title;
		}

		public void setUserType(String UserType) {
			this.UserType = UserType;
		}

		public String getUserType() {
			return UserType;
		}

		public void setURL(String URL) {
			this.URL = URL;
		}

		public String getURL() {
			return URL;
		}

		public void setIsShow(String IsShow) {
			this.IsShow = IsShow;
		}

		public String getIsShow() {
			return IsShow;
		}
	}

	@Override
	public String toString() {
		return "SoftInfo [SoftName=" + SoftName + ", Version=" + Version + ", Content=" + Content + ", DownLoadUrl="
				+ DownLoadUrl + ", vmid=" + vmid + ", fmid=" + fmid + ", ForceUpdate=" + ForceUpdate
				+ ", SoftStartImage=" + SoftStartImage + ", LogCmd=" + LogCmd + ", DocumentCofig=" + DocumentCofig
				+ "]";
	}
	
}
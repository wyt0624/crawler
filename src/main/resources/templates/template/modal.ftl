<!-- Modal -->
<div id="exampleModal" >
        <div class="modal-content">
            <div class="modal-body">
                <form id = "add_phishing" >
                    <input type = "hidden" id = "phishing_id" value ="">
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">请输入地址:</label>
                        <em style="color: red;">*</em>
                        <input type="text" id = "phishing_url"  class="form-control" >
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">请输入网站名称:</label>
                        <em style="color: red;">*</em>
                        <input type="text" id = "phishing_name"  class="form-control" >
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="col-form-label">请输入网站标题:</label>
                        <em style="color: red;">*</em>
                        <input type="text" id = "phishing_title"  class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="message-text" class="col-form-label">请输入网站关键词:</label>
                            <em style="color: red;">*</em>
                            <button type="button" class="btn btn-link"   id="participle">智能分词</button>
                            <button type="button" class="btn btn-link"   id="handMovement">手动分词</button>
                        <textarea class="form-control"  rows ="9"  id="phishing_keyword"></textarea>
                    </div>
                </form>
            </div>
        </div>
</div>



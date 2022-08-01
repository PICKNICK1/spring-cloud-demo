/*
 *
 *  *  Copyright (c) 2019-2020, yekai (xxx@qq.com).
 *  *  <p>
 *  *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  * https://www.gnu.org/licenses/lgpl.html
 *  *  <p>
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package com.lk.common.minio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MinioProperties
 *
 * @date 2020-04-07
 */
@ConfigurationProperties("minio")
@Data
public class MinioProperties {
    /**
     * URL,域名，或者ip地址
     */
    private String endpoint;
    /**
     * 端口号
     */
    private Integer port;
    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;
    /**
     * 默认的存储桶
     */
    private String bucketName;

}

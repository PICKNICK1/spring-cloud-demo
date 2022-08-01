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
package com.lk.common.minio;


import com.lk.common.minio.config.MinioProperties;
import com.lk.common.minio.service.MinioUtils;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minio配置
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MinioProperties.class)
public class MinioAutoConfiguration {

    @Bean
    public MinioClient minioClient(MinioProperties minioProperties) {
        try {
            return MinioClient.builder()
                    .endpoint(minioProperties.getEndpoint(),minioProperties.getPort(),false)
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                    .build();
        } catch (Exception e) {
            log.error("初始化minio错误,ex={}",e.getMessage(), e);
            return null;
        }
    }

    @Bean
    public MinioUtils minioUtils(MinioClient minioClient) {
        return new MinioUtils(minioClient);
    }

}
